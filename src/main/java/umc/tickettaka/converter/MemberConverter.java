package umc.tickettaka.converter;

import java.util.Collections;
import umc.tickettaka.config.security.jwt.JwtToken;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.enums.ProviderType;
import umc.tickettaka.web.dto.request.SignRequestDto.SignUpDto;
import umc.tickettaka.web.dto.response.SignResponseDto;
import umc.tickettaka.web.dto.response.SignResponseDto.SignInResultDto;
import umc.tickettaka.web.dto.response.SignResponseDto.SignUpResultDto;

public class MemberConverter {

    public static SignResponseDto.SignInResultDto toSignInResultDto(JwtToken token) {
        return SignInResultDto.builder()
            .token(token)
            .build();
    }

    public static SignResponseDto.SignUpResultDto toSignUpResultDto(Member member) {
        return SignUpResultDto.builder()
            .memberId(member.getId())
            .createdTime(member.getCreatedTime())
            .build();
    }

    public static Member toMember(SignUpDto signUpDto, String encodedPassword) {
        // parse values
        String username = signUpDto.getUsername();
        String email = signUpDto.getEmail();
        String imageUrl = signUpDto.getImageUrl();
        String providerType = signUpDto.getProviderType();
        String providerId = signUpDto.getProviderId();

        ProviderType providerTypeEnum = null;
        if (providerType != null) {
            providerTypeEnum = ProviderType.valueOf(providerType);
        }

        return Member.builder()
            .username(username)
            .password(encodedPassword)
            .email(email)
            .imageUrl(imageUrl)
            .providerType(providerTypeEnum)
            .providerId(providerId)
            .roles(Collections.singletonList("ROLE_USER"))
            .build();
    }
}
