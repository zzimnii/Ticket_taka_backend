package umc.tickettaka.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import umc.tickettaka.config.security.jwt.JwtToken;
import umc.tickettaka.config.security.jwt.JwtTokenProvider;
import umc.tickettaka.config.security.jwt.CustomUserDetailService;
import umc.tickettaka.domain.Member;
import umc.tickettaka.payload.ApiResponse;
import umc.tickettaka.payload.exception.GeneralException;
import umc.tickettaka.payload.status.ErrorStatus;
import umc.tickettaka.service.MemberCommandService;
import umc.tickettaka.service.MemberQueryService;
import umc.tickettaka.web.dto.request.SignRequestDto;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberQueryService memberQueryService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
        String rawProvider = (String) oAuth2User.getAttribute("provider");
        if (rawProvider == null) {
            throw new GeneralException(ErrorStatus.SNS_LOGIN_WRONG_INFORMATION);
        }
        String provider = ((String) oAuth2User.getAttribute("provider")).toUpperCase();
        String email = (String) oAuth2User.getAttribute("email");

        // email이 중복되는 경우가 있으므로 providerType과 email 모두 같은 경우 중복으로 처리
        Optional<Member> foundMember = memberQueryService.findByEmailAndProvider(email, provider);
        // sns 회원 가입이 되어 있는 경우 (DB에 저장되어있음)
        if (foundMember.isPresent()) {
            Member member = foundMember.get();
            String username = member.getUsername();
            JwtToken jwtToken = jwtTokenProvider.generateToken(authentication, username, false);

            Map<String, Object> tokenResponse = new HashMap<>();

            tokenResponse.put("accessToken", jwtToken.getAccessToken());
            tokenResponse.put("message", "SNS login success");
            tokenResponse.put("isInDB", true);
            tokenResponse.put("provider", provider);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(objectMapper.writeValueAsString(tokenResponse));

            log.info("sns login success()- id already in the DB");
        }

        else {
            JwtToken jwtToken = jwtTokenProvider.generateToken(authentication, null, false);
            Map<String, Object> tokenResponse = new HashMap<>();
            tokenResponse.put("accessToken", jwtToken.getAccessToken());
            tokenResponse.put("message", "SNS login success");
            tokenResponse.put("isInDB", false);
            tokenResponse.put("provider", provider);
            tokenResponse.put("email", email);



            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(objectMapper.writeValueAsString(tokenResponse));

            log.info("sns login && sign Up success()- id not in the DB");

        }
    }
}
