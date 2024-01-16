package umc.tickettaka.service;

import umc.tickettaka.config.security.jwt.JwtToken;
import umc.tickettaka.domain.Member;
import umc.tickettaka.web.dto.request.SignRequestDto;

public interface MemberCommandService {
    JwtToken signIn(SignRequestDto.SignInDto signInDto);
    Member save(SignRequestDto.SignUpDto signUpDto);

}
