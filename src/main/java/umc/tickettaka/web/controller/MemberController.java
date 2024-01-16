package umc.tickettaka.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.tickettaka.config.security.jwt.JwtToken;
import umc.tickettaka.converter.MemberConverter;
import umc.tickettaka.domain.CustomUserDetailsAdapter;
import umc.tickettaka.domain.Member;
import umc.tickettaka.payload.ApiResponse;
import umc.tickettaka.service.MemberService;
import umc.tickettaka.web.dto.request.SignRequestDto;
import umc.tickettaka.web.dto.response.SignResponseDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    // 로그인
    @PostMapping("/sign-in")
    public ApiResponse<SignResponseDto.SignInResultDto> jwtSignIn(@RequestBody SignRequestDto.SignInDto signInDto) {
        String username = signInDto.getUsername();
        String password = signInDto.getPassword();

        JwtToken jwtToken = memberService.signIn(username , password);
        log.info("request username = {}, password = {}", username, password);
        log.info("jwtToken accessToken = {}", jwtToken.getAccessToken());
        return ApiResponse.onSuccess(MemberConverter.toSignInResultDto(jwtToken));
    }

    // 회원가입
    @PostMapping("/sign-up")
    public ApiResponse<SignResponseDto.SignUpResultDto> SignUp(@RequestBody SignRequestDto.SignUpDto signUpDto) {

        Member member = memberService.save(signUpDto);
        return ApiResponse.onCreate(MemberConverter.toSignUpResultDto(member));
    }

    @GetMapping("/token-test")
    public ApiResponse<Long> test(@AuthenticationPrincipal CustomUserDetailsAdapter customUserDetails) {
        Member member = customUserDetails.getMember();

        return ApiResponse.onSuccess(member.getId());
    }
}
