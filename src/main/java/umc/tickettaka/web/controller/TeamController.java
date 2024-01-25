package umc.tickettaka.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.tickettaka.config.security.jwt.AuthUser;
import umc.tickettaka.converter.InvitationConverter;
import umc.tickettaka.converter.TeamConverter;
import umc.tickettaka.domain.Invitation;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.Team;
import umc.tickettaka.payload.ApiResponse;
import umc.tickettaka.service.InvitationCommandService;
import umc.tickettaka.service.InvitationQueryService;
import umc.tickettaka.service.TeamQueryService;
import umc.tickettaka.web.dto.request.TeamRequestDto;
import umc.tickettaka.web.dto.response.InvitationResponseDto;
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

    @PostMapping(value = "/create", consumes = {MediaType.APPLICATION_JSON_VALUE , MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "팀 생성 API", description = "팀 생성하기")
    public ApiResponse<TeamResponseDto.TeamDto> createTeam(
        @AuthUser Member member,
        @RequestPart(value = "image", required = false) MultipartFile image,
        @RequestPart(value = "request") TeamRequestDto.CreateTeamDto request) throws IOException {
        Team team = teamCommandService.createTeam(member, image, request);
        return ApiResponse.onCreate(TeamConverter.toTeamResultDto(team));
    }

    @GetMapping("")
    @Operation(summary = "생성된 팀 목록, 초대 팀 목록 조회", description = "생성된 팀, 초대 목록 조회하기")
    public ApiResponse<TeamResponseDto.TeamAndInvitationListDto> getTeamList(
            @AuthUser Member receiver
    ) {
        List<Team> teamList = teamQueryService.findTeamsByMember(receiver);
        List<Invitation> invitationList = invitationQueryService.findAll();
        return ApiResponse.onSuccess(TeamConverter.teamAndInvitationListDto(receiver, teamList, invitationList));
    }

    @GetMapping("/{teamsId}")
    @Operation(summary = "특정 팀 조회 API",description = "특정 팀 조회하는 API")
    @Parameters({
            @Parameter(name = "teamsId", description = "팀의 아이디, path variable 입니다.")
    })
    public ApiResponse<TeamResponseDto.TeamDto> getTeam(@PathVariable(name = "teamsId") Long teamsId ) {
        Team team = teamQueryService.findTeam(teamsId);
        return ApiResponse.onSuccess(TeamConverter.toTeamResultDto(team));
    }

    @PatchMapping(value = "/{teamsId}/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "팀 정보 수정 API", description = "팀 정보를 업데이트하는 API")
    @Parameters({
            @Parameter(name = "teamsId", description = "팀의 아이디, path variable 입니다.")
    })
    public ApiResponse<TeamResponseDto.TeamDto> updateTeam(
            @PathVariable(name = "teamsId") Long teamsId,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart TeamRequestDto.CreateTeamDto request) throws IOException{
        Team updatedTeam = teamCommandService.updateTeam(teamsId, image, request);
        return ApiResponse.onSuccess(TeamConverter.toTeamResultDto(updatedTeam));
    }

    @DeleteMapping("/{teamsId}")
    @Operation(summary = "팀 삭제", description = "팀을 삭제하기, 삭제하면 Response는 가입된 전체 팀이 조회됩니다.")
    @Parameter(name = "teamsId", description = "팀의 아이디, path variable 입니다.")
    public ApiResponse<TeamResponseDto.TeamAndInvitationListDto> deleteTeam(
            @PathVariable(name = "teamsId") Long teamsId,
            @AuthUser Member member) throws IOException{
        teamCommandService.deleteTeam(teamsId);
        List<Team> teamList = teamQueryService.findAll();
        List<Invitation> invitationList = invitationQueryService.findAll();
        TeamResponseDto.TeamAndInvitationListDto teamListDto = TeamConverter.teamAndInvitationListDto(member, teamList, invitationList);
        return ApiResponse.onSuccess(teamListDto);
    }

    @PostMapping("/{teamsId}/invite")
    @Operation(summary = "팀에 멤버 초대", description = "팀에 멤버 초대하기")
    @Parameter(name = "teamsId", description = "팀의 아이디, path variable 입니다.")
    public ApiResponse<TeamResponseDto.TeamDto> inviteTeam(
            @PathVariable(name = "teamsId") Long teamsId,
            @AuthUser Member sender,
            @RequestParam(name = "receiver") String receiverUsername) {

        Team team = teamQueryService.findTeam(teamsId);
        invitationCommandService.sendInvitation(sender, team, receiverUsername);
        return ApiResponse.onSuccess(TeamConverter.toTeamResultDto(team));
    }

    @GetMapping("/invite")
    @Operation(summary = "초대된 팀 목록", description = "초대된 팀 목록(수락/거절) 조회하기")
    public ApiResponse<InvitationResponseDto.InvitationListDto> getInvitationList(
            @AuthUser Member member) {
        List<Invitation> invitationList = invitationQueryService.findAll();
        return ApiResponse.onSuccess(InvitationConverter.toInvitationListDto(member, invitationList));
    }

    @PostMapping("")
    @Operation(summary = "팀에 멤버 초대 수락/거절", description = "팀에 멤버 초대 수락/거절")
    public ApiResponse<TeamResponseDto.TeamAndInvitationListDto> acceptOrRejectTeam(
            @RequestParam(name = "invitationId") Long invitationId,
            @RequestParam(name = "isAccepted") boolean isAccepted,
            @AuthUser Member receiver) {

        invitationCommandService.isAcceptedInvitation(invitationId, receiver, isAccepted);

        List<Invitation> invitationList = invitationQueryService.findAll();
        List<Team> teamList = teamQueryService.findTeamsByMember(receiver);

        return ApiResponse.onSuccess(TeamConverter.teamAndInvitationListDto(receiver, teamList, invitationList));
    }
}