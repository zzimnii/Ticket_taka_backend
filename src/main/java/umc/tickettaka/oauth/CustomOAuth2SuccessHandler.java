package umc.tickettaka.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import umc.tickettaka.config.security.jwt.JwtToken;
import umc.tickettaka.config.security.jwt.JwtTokenProvider;
import umc.tickettaka.config.security.jwt.CustomUserDetailService;
import umc.tickettaka.domain.Member;
import umc.tickettaka.payload.exception.GeneralException;
import umc.tickettaka.payload.status.ErrorStatus;
import umc.tickettaka.service.MemberQueryService;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberQueryService memberQueryService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
        String rawProvider = (String) oAuth2User.getAttribute("provider");
        if (rawProvider == null) {
            throw new GeneralException(ErrorStatus.SNS_LOGIN_WRONG_INFORMATION);
        }
        String provider = ((String) oAuth2User.getAttribute("provider")).toUpperCase();
        String providerId = (String) oAuth2User.getAttribute("providerId");

        Optional<Member> foundMember = memberQueryService.findByProviderId(providerId);
        // sns 회원 가입이 되어 있는 경우 (DB에 저장되어있음)
        if (foundMember.isPresent()) {
            Member member = foundMember.get();
            String username = member.getUsername();
            JwtToken jwtToken = jwtTokenProvider.generateToken(authentication, username);

            String  targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8080")
                    .queryParam("token", jwtToken.getAccessToken())
                    .build().toUriString();

            getRedirectStrategy().sendRedirect(request, response, targetUrl);
            log.info("sns login success()");
        }
        // 1. sns 회원 가입이 되어 있지 않은 경우 -> provider, providerId를 front에 제공.
        // 2. 이를 받아 회원가입 form의 정보와 함께 dto에 담고, 회원 가입 post를 요청하면 받아서 save
        // 3. save에 성공하면 로그인 page로 redirect, save api에는 jwt 발급 X
        else {
            String  targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8080")
                    .queryParam("provider", provider)
                    .queryParam("providerId", providerId)
                    .build().toUriString();

            getRedirectStrategy().sendRedirect(request, response, targetUrl);
            log.info("redirect to 회원가입 창 with provider data()");

        }
    }
}
