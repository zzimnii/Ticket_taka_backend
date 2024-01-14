package umc.tickettaka.web.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import umc.tickettaka.config.security.jwt.JwtToken;
import umc.tickettaka.config.security.jwt.JwtTokenProvider;
import umc.tickettaka.domain.CustomUserDetails;
import umc.tickettaka.web.repository.MemberRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final CustomUserDetailService customUserDetailService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder passwordEncoder;


    @Transactional
    public JwtToken signIn(String userName, String password) {
        // DB에서 회원정보를 가져온 후 -> CustomUserDetail로 변환한 객체를 받는다.
        UserDetails userDetails = customUserDetailService.loadUserByUsername(userName);

        // 비밀번호가 다른 경우 UNAUTHORIZED 응답을 보낸다.
        if (userName == null || password == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        // 1.사용자가 아이디와 비밀번호를 입력하면 UsernamePasswordAuthentication 토큰을 생성한다.
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, password);

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        return jwtToken;
    }

}
