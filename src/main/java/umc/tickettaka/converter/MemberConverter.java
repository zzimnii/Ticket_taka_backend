package umc.tickettaka.converter;

import java.util.Collections;
import umc.tickettaka.config.security.jwt.JwtToken;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.enums.Color;
import umc.tickettaka.domain.enums.ProviderType;
import umc.tickettaka.web.dto.common.CommonMemberDto.ShowMemberProfileDto;
import umc.tickettaka.web.dto.request.SignRequestDto.SignUpDto;
import umc.tickettaka.web.dto.response.MemberResponseDto;
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

    public static ShowMemberProfileDto toMemberProfileDto(Member member) {
        return ShowMemberProfileDto.builder()
            .name(member.getName())
            .imageUrl(member.getImageUrl())
            .memberHex(Color.PASTEL_1.getHex())
            .build();
    }



    public static Member toMember(SignUpDto signUpDto, String encodedPassword) {
        // parse values
        String name = signUpDto.getName();
        String username = signUpDto.getUsername();
        String imageUrl = signUpDto.getImageUrl();
        String providerType = signUpDto.getProviderType();
        String email = signUpDto.getEmail();
        String deviceToken = signUpDto.getDeviceToken();

        ProviderType providerTypeEnum = null;
        if (providerType != null) {
            providerTypeEnum = ProviderType.valueOf(providerType);
        }

        return Member.builder()
            .name(name)
            .username(username)
            .password(encodedPassword)
            .imageUrl(imageUrl)
            .providerType(providerTypeEnum)
            .email(email)
            .deviceToken(deviceToken)
            .roles(Collections.singletonList("ROLE_USER"))
            .build();
    }

    public static MemberResponseDto.ShowMemberDto toShowProjectDto(Member member) {
        return MemberResponseDto.ShowMemberDto.builder()
                .memberId(member.getId())
                .name(member.getName())
                .username(member.getUsername())
                .imageUrl(member.getImageUrl())
                .build();
    }

    public static MemberResponseDto.TokenDto toTokenDto(String accessToken) {
        return MemberResponseDto.TokenDto.builder()
                .accessToken(accessToken)
                .build();
    }
}
