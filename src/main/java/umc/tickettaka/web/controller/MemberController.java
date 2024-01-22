package umc.tickettaka.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.tickettaka.config.security.jwt.AuthUser;
import umc.tickettaka.config.security.jwt.JwtToken;
import umc.tickettaka.converter.MemberConverter;
import umc.tickettaka.domain.Member;
import umc.tickettaka.payload.ApiResponse;
import umc.tickettaka.service.MemberCommandService;
import umc.tickettaka.web.dto.request.MemberRequestDto;
import umc.tickettaka.web.dto.request.SignRequestDto;
import umc.tickettaka.web.dto.response.MemberResponseDto;
import umc.tickettaka.web.dto.response.SignResponseDto;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberCommandService memberCommandService;

    // 로그인
    @PostMapping("/sign-in")
    public ApiResponse<SignResponseDto.SignInResultDto> jwtSignIn(@RequestBody SignRequestDto.SignInDto signInDto) {
        JwtToken jwtToken = memberCommandService.signIn(signInDto);
        return ApiResponse.onSuccess(MemberConverter.toSignInResultDto(jwtToken));
    }

    // 회원가입
    @PostMapping("/sign-up")
    public ApiResponse<SignResponseDto.SignUpResultDto> signUp(@RequestBody SignRequestDto.SignUpDto signUpDto) {

        Member member = memberCommandService.save(signUpDto);
        return ApiResponse.onCreate(MemberConverter.toSignUpResultDto(member));
    }

    @PatchMapping(value = "/{memberId}/update", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<MemberResponseDto.ShowMemberDto> updateMember(
            @PathVariable("memberId") Long memberId,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "request", required = false) MemberRequestDto.UpdateDto memberUpdateDto
            ) throws IOException {
        
            Member member = memberCommandService.updateMember(memberId, image,memberUpdateDto);

        return ApiResponse.onSuccess(MemberConverter.toShowProjectDto(member));
    }


    /**
     * 나중에 지울거임~
     */
    @GetMapping("/token-test")
    public ApiResponse<Long> test(@AuthUser Member member) {
        log.info(member.getUsername());
        return ApiResponse.onSuccess(member.getId());
    }
}