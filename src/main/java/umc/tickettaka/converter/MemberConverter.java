package umc.tickettaka.converter;

import umc.tickettaka.config.security.jwt.JwtToken;
import umc.tickettaka.domain.Member;
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
}
