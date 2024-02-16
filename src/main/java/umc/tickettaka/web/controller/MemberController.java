package umc.tickettaka.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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
import umc.tickettaka.service.MemberQueryService;
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
    private final MemberQueryService memberQueryService;

    @GetMapping("/my-page")
    @Operation(summary = "마이페이지 조회")
    @Parameters({
        @Parameter(name = "status", description = "티켓 상태 : todo, inprogress, done 셋 중 하나입니다."),
        @Parameter(name = "teamId", description = "조회할 팀 아이디 : Query Parameter")
    })
    public ApiResponse<MemberResponseDto.MyPageMemberDto> getMemberForMyPage(@AuthUser Member member,
                                                                             @RequestParam(name = "status", required = false) String status,
                                                                             @RequestParam(name = "teamId", required = false) Long teamId) {

        Member memberWithTicketsAndMemberTeamsByMemberId = memberQueryService.findMemberWithTicketsAndMemberTeamsByMemberId(member.getId());

        return ApiResponse.onSuccess(MemberConverter.toMyPageMemberDto(memberWithTicketsAndMemberTeamsByMemberId, status, teamId));
    }

    @PostMapping("/refresh")
    public ApiResponse<MemberResponseDto.TokenDto> getAccessTokenByRefreshToken(@RequestBody MemberRequestDto.TokenDto tokenDto) {
        String expiredAccessToken = tokenDto.getAccessToken();
        log.info("expiredAccessToken = {} ", expiredAccessToken);

        String accessToken = memberCommandService.getAccessToken(expiredAccessToken);
        return ApiResponse.onCreate(MemberConverter.toTokenDto(accessToken));
    }

    // 로그인
    @PostMapping("/sign-in")
    @Operation(summary = "로그인")
    public ApiResponse<SignResponseDto.SignInResultDto> jwtSignIn(@RequestBody SignRequestDto.SignInDto signInDto) {

        JwtToken jwtToken = memberCommandService.signIn(signInDto);
        return ApiResponse.onSuccess(MemberConverter.toSignInResultDto(jwtToken));
    }

    // 회원가입
    @PostMapping("/sign-up")
    @Operation(summary = "회원가입")
    public ApiResponse<SignResponseDto.SignUpResultDto> signUp(@RequestBody SignRequestDto.SignUpDto signUpDto) {

        Member member = memberCommandService.save(signUpDto);
        return ApiResponse.onCreate(MemberConverter.toSignUpResultDto(member));
    }

    @PatchMapping(value = "/{memberId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "회원정보 수정")
    @Parameters({
        @Parameter(name = "status", description = "티켓 상태 : todo, inprogress, done 셋 중 하나입니다."),
        @Parameter(name = "memberId", description = "조회할 팀 아이디 : Path Variable")
    })
    public ApiResponse<MemberResponseDto.ShowMemberDto> updateMember(
            @PathVariable("memberId") Long memberId,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "request", required = false) MemberRequestDto.UpdateDto memberUpdateDto
            ) throws IOException {
        
            Member member = memberCommandService.updateMember(memberId, image, memberUpdateDto);

        return ApiResponse.onSuccess(MemberConverter.toShowProjectDto(member));
    }


}