package umc.tickettaka.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.tickettaka.config.security.jwt.JwtToken;
import umc.tickettaka.payload.ApiResponse;
import umc.tickettaka.payload.status.SuccessStatus;
import umc.tickettaka.service.MemberService;
import umc.tickettaka.web.dto.request.SignRequestDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    // 로그인
    @PostMapping("/sign-in")
    public JwtToken jwtSignIn(@RequestBody SignRequestDto.SignInDto signInDto) {
        String username = signInDto.getUsername();
        String password = signInDto.getPassword();

        JwtToken jwtToken = memberService.signIn(username , password);
        log.info("request username = {}, password = {}", username, password);
        log.info("jwtToken accessToken = {}", jwtToken.getAccessToken());
        return jwtToken;
    }

    // 회원가입
    @PostMapping("/sign-up")
    public ApiResponse<Long> SignUp(@RequestBody SignRequestDto.SignUpDto signUpDto) {

        Long memberId = memberService.save(signUpDto);

        ApiResponse<Long> response = ApiResponse.<Long>builder()
                .isSuccess(true)
                .code(SuccessStatus.CREATED.getCode())
                .message(SuccessStatus.CREATED.getMessage())
                .result(memberId)
                .build();

        return response;
    }
}
