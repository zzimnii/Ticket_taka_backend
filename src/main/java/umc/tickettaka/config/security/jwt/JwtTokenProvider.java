package umc.tickettaka.config.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;

    private final CustomUserDetailService customUserDetailService;


    // application.yml에서 secret 값 가져와서 key에 저장
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, CustomUserDetailService customUserDetailService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.customUserDetailService = customUserDetailService;
    }

    // Member 정보를 가지고 AccessToken, RefreshToken을 생성하는 메서드
    public JwtToken generateToken(Authentication authentication, String username, Boolean keepStatus) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        // sns 로그인 여부 체크를 위한 provider parsing
        Object principal = authentication.getPrincipal();
        String provider = null;
        String email = null;

        // email은 오직 sns 로그인에서만 사용
        if (principal instanceof OAuth2User oAuth2User) {
            provider = oAuth2User.getAttribute("provider");
            email = oAuth2User.getAttribute("email");
        }


        if (provider != null) {
            provider = provider.toUpperCase();
        }

        // Token 생성
        // Expire 시간 생성
        LocalDateTime now = LocalDateTime.now();
        // accessToken Expire 시간 생성
        LocalDateTime afterHalfHour = now.plus(30, ChronoUnit.MINUTES);
        Date accessTokenExpiresIn = convertToDate(afterHalfHour);

        // refreshToken Expire 시간 생성
        Date refreshTokenExpiresIn;

        if (keepStatus) {
            LocalDateTime afterWeekHour = now.plus(7, ChronoUnit.DAYS);
            refreshTokenExpiresIn = convertToDate(afterWeekHour); // 만약 login 유지에 체크한 경우 일주일
        }
        else {
            LocalDateTime afterDayHour = now.plus(1, ChronoUnit.DAYS);
            refreshTokenExpiresIn = convertToDate(afterDayHour); // 아닌 경우 하루
        }

        // accessToken은 정보를 담고 있어, 유효 시간이 짧고
        String accessToken = Jwts.builder()
                .claim("auth", authorities)
                .claim("username", username)
                .claim("provider", provider)
                .claim("email", email)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // refresh토큰은 유효시간이 길지만, 정보를 담고 있지 않다.
        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtToken.builder()
            .grantType("Bearer")
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    private Date convertToDate(LocalDateTime afterHalfHour) {
        ZonedDateTime zonedDateTime = afterHalfHour.atZone(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    // Jwt 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        // Jwt 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication return
        UserDetails principal = customUserDetailService.loadUserByUsername((String) claims.get("username"));

        return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {

            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
    }


    // accessToken
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String recreateAccessToken(String username, String email, String provider, List<String> roles) {
        String authorities = String.join(",", roles);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime afterHalfHour = now.plus(30, ChronoUnit.SECONDS);
        Date accessTokenExpiresIn = convertToDate(afterHalfHour);

        return Jwts.builder()
                .claim("auth", authorities)
                .claim("username", username)
                .claim("provider", provider)
                .claim("email", email)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
