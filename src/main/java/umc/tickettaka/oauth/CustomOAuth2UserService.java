package umc.tickettaka.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService ()");
        // 기본 OAuth2UserService 객체 생성하기
        DefaultOAuth2UserService auth2UserService = new DefaultOAuth2UserService();

        // OAuth2UserService를 사용해 OAuth2User 정보 가져오기
        OAuth2User oAuth2User = auth2UserService.loadUser(userRequest);

        // Provider id 정보 가져오기 naver인지, kakao인지 구분
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        // raw attributes 로 각 social 로그인 마다 구조가 다르다.
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 각 social 로그인에 따른 parsing 한 attributes 들
        Map<String, Object> targetAttributes = parseTargetAttributes(attributes, registrationId);

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                targetAttributes,
                userNameAttributeName);
    }

    private Map<String, Object> parseTargetAttributes(Map<String, Object> attributes, String registrationId) {
        Map<String, Object> targetAttributes = new HashMap<>();

        if (registrationId.equals("kakao")) {
            Long id = (Long) attributes.get("id");
            String providerId = String.valueOf(id);
            targetAttributes.put("id", providerId);  // defaultOAuth2User 생성을 위한 targetAttributes에는 userNameAttributeName이 포함되어있어야 한다. ( 코드가 그렇게 되어있음)
            Map<String, Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakao_account.get("profile");
            String nickname = (String) profile.get("nickname");
            targetAttributes.put("nickname", nickname);

            log.info("nickname = {}", nickname);
        }

        return targetAttributes;
    }
}
