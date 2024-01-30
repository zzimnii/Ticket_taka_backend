package umc.tickettaka.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.io.IOException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.tickettaka.config.security.jwt.AuthUser;
import umc.tickettaka.converter.TeamConverter;
import umc.tickettaka.domain.Invitation;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.Team;
import umc.tickettaka.payload.ApiResponse;
import umc.tickettaka.service.InvitationCommandService;
import umc.tickettaka.service.InvitationQueryService;
import umc.tickettaka.service.TeamQueryService;
import umc.tickettaka.web.dto.request.InvitationRequestDto;
import umc.tickettaka.web.dto.request.MemberTeamRequestDto;
import umc.tickettaka.web.dto.request.TeamRequestDto;
import umc.tickettaka.web.dto.response.TeamResponseDto;
import umc.tickettaka.service.TeamCommandService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamController {

    private final TeamCommandService teamCommandService;
    private final TeamQueryService teamQueryService;
    private final InvitationCommandService invitationCommandService;
    private final InvitationQueryService invitationQueryService;

    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE , MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "팀 생성 API", description = "팀 생성하기")
    public ApiResponse<TeamResponseDto.TeamDto> createTeam(
            @AuthUser Member member,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "request") @Valid TeamRequestDto.CreateTeamDto request) throws IOException {

        Team team = teamCommandService.createTeam(member, image, request);
        return ApiResponse.onCreate(TeamConverter.toTeamResultDto(team));
    }

    @GetMapping("")
    @Operation(summary = "생성된 팀 목록, 초대 팀 목록 조회", description = "생성된 팀, 초대 목록 조회하기")
    public ApiResponse<TeamResponseDto.TeamAndInvitationListDto> getTeamList(
            @AuthUser Member receiver ) {
        List<Team> teamList = teamQueryService.findTeamsByMember(receiver);
        List<Invitation> invitationList = invitationQueryService.findAll();
        return ApiResponse.onSuccess(TeamConverter.teamAndInvitationListDto(receiver, teamList, invitationList));
    }

    @PatchMapping(value = "/{teamId}/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "팀 정보 수정 API", description = "팀 정보를 업데이트하는 API")
    @Parameter(name = "teamId", description = "팀의 아이디, path variable 입니다.")
    public ApiResponse<TeamResponseDto.TeamDto> updateTeam(
            @PathVariable(name = "teamId") Long teamId,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "request", required = false) @Valid TeamRequestDto.CreateTeamDto updateTeamDto) throws IOException {

        Team updatedTeam = teamCommandService.updateTeam(teamId, image, updateTeamDto);

        return ApiResponse.onSuccess(TeamConverter.toTeamResultDto(updatedTeam));
    }

    @DeleteMapping("/{teamId}")
    @Operation(summary = "팀 삭제", description = "팀을 삭제하기, 삭제하면 Response는 가입된 전체 팀이 조회됩니다.")
    @Parameter(name = "teamId", description = "팀의 아이디, path variable 입니다.")
    public ApiResponse<TeamResponseDto.TeamAndInvitationListDto> deleteTeam(
            @PathVariable(name = "teamId") Long teamId,
            @AuthUser Member member) throws IOException {

        teamCommandService.deleteTeam(teamId);
        List<Team> teamList = teamQueryService.findAll();
        List<Invitation> invitationList = invitationQueryService.findAll();
        return ApiResponse.onSuccess(TeamConverter.teamAndInvitationListDto(member, teamList, invitationList));
    }

    @PostMapping("/{teamId}")
    @Operation(summary = "팀에 멤버 초대", description = "팀에 멤버 초대하기")
    @Parameter(name = "teamId", description = "팀의 아이디, path variable 입니다.")
    public ApiResponse<TeamResponseDto.TeamDto> inviteTeam(
            @PathVariable(name = "teamId") Long teamId,
            @AuthUser Member sender,
            @RequestBody @Valid InvitationRequestDto.CreateInvitationDto invitationDto) {

        Team team = teamQueryService.findTeam(teamId);
        invitationCommandService.sendInvitation(sender, team, invitationDto.getInvitedUsernameList());
        return ApiResponse.onSuccess(TeamConverter.toTeamResultDto(team));
    }

    @PostMapping("/invitation-response")
    @Operation(summary = "팀에 멤버 초대 수락/거절", description = "팀에 멤버 초대 수락/거절")
    public ApiResponse<TeamResponseDto.TeamAndInvitationListDto> acceptOrRejectTeam(
            @RequestBody @Valid InvitationRequestDto.AcceptInvitationDto request,
            @AuthUser Member receiver) {

        invitationCommandService.isAcceptedInvitation(request.getInvitationId(), receiver, request);

        List<Invitation> invitationList = invitationQueryService.findAll();
        List<Team> teamList = teamQueryService.findTeamsByMember(receiver);

        return ApiResponse.onSuccess(TeamConverter.teamAndInvitationListDto(receiver, teamList, invitationList));
    }

    @PatchMapping("/{teamId}/color")
    @Operation(summary = "팀내 색 수정 API", description = "팀내 본인 색 수정 API")
    @Parameter(name = "teamId", description = "팀의 아이디, path variable 입니다.")
    public ApiResponse<TeamResponseDto.TeamDto> updateTeam(
            @AuthUser Member member,
            @PathVariable(name = "teamId") Long teamId,
            @RequestBody @Valid MemberTeamRequestDto.UpdateColorDto updateDto) {

        Team team = teamQueryService.findTeam(teamId);
        teamCommandService.updateMemberTeamColor(member, teamId, updateDto);

        return ApiResponse.onSuccess(TeamConverter.toTeamResultDto(team));
    }
}