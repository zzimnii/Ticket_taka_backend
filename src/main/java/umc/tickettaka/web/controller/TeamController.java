package umc.tickettaka.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.io.IOException;

import io.swagger.v3.oas.annotations.Parameters;
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
import umc.tickettaka.domain.mapping.MemberTeam;
import umc.tickettaka.domain.ticket.Ticket;
import umc.tickettaka.payload.ApiResponse;
import umc.tickettaka.service.*;
import umc.tickettaka.web.dto.common.CommonMemberDto.ShowMemberProfileListDto;
import umc.tickettaka.web.dto.request.InvitationRequestDto;
import umc.tickettaka.web.dto.request.MemberTeamRequestDto;
import umc.tickettaka.web.dto.request.TeamRequestDto;
import umc.tickettaka.web.dto.response.TeamResponseDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamController {

    private final TeamCommandService teamCommandService;
    private final TeamQueryService teamQueryService;
    private final InvitationCommandService invitationCommandService;
    private final InvitationQueryService invitationQueryService;
    private final TicketQueryService ticketQueryService;
    private final MemberTeamQueryService memberTeamQueryService;
    private final MemberCommandService memberCommandService;

    @GetMapping("")
    @Operation(summary = "생성된 팀 목록, 초대 팀 목록 조회", description = "생성된 팀, 초대 목록 조회하기")
    public ApiResponse<TeamResponseDto.TeamAndInvitationListDto> getTeamList(
            @AuthUser Member receiver ) {
        List<Team> teamList = teamQueryService.findTeamsByMember(receiver);
        List<Invitation> invitationList = invitationQueryService.findAll();
        return ApiResponse.onSuccess(TeamConverter.teamAndInvitationListDto(receiver, teamList, invitationList));
    }

    @GetMapping("/{teamId}/calendar")
    @Operation(summary = "팀 캘린더 조회", description = "팀 캘린더 조회API")
    @Parameters({
            @Parameter(name = "teamId", description = "팀의 아이디, path variable 입니다."),
            @Parameter(name = "status", description = "티켓 상태 : todo, inprogress, done 셋 중 하나입니다."),
            @Parameter(name = "sort", description = "마감 순 정렬 : asc, desc 둘 중 하나입니다."),
            @Parameter(name = "memberId", description = "멤버Id")
    })
    public ApiResponse<TeamResponseDto.TeamCalendarDto> teamCalendar(
            @AuthUser Member member,
            @PathVariable(name = "teamId") Long teamId,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "memberId", required = false) Long memberId) {
        Team team = teamQueryService.findTeam(teamId);
        List<MemberTeam> memberTeams = memberTeamQueryService.findAllMembersByTeam(team);
        List<Ticket> ticketList = ticketQueryService.findAllByTeam(team);
        ShowMemberProfileListDto memberProfileListDto = memberCommandService.getCommonMemberDto(teamId);

        return ApiResponse.onSuccess(TeamConverter.teamCalendarDto(member, memberTeams, team, ticketList, memberProfileListDto, status, sort, memberId));
    }

    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE , MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "팀 생성 API", description = "팀 생성하기")
    public ApiResponse<TeamResponseDto.TeamDto> createTeam(
            @AuthUser Member member,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "request") @Valid TeamRequestDto.CreateTeamDto request) throws IOException {

        Team team = teamCommandService.createTeam(member, image, request);
        return ApiResponse.onCreate(TeamConverter.toTeamResultDto(team));
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

}