package umc.tickettaka.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import umc.tickettaka.config.security.jwt.AuthUser;
import umc.tickettaka.converter.TicketConverter;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.ticket.Ticket;
import umc.tickettaka.payload.ApiResponse;
import umc.tickettaka.service.MemberCommandService;
import umc.tickettaka.service.TicketCommandService;
import umc.tickettaka.service.TicketQueryService;
import umc.tickettaka.web.dto.common.CommonMemberDto;
import umc.tickettaka.web.dto.common.CommonTicketDto.ShowTicketDto;
import umc.tickettaka.web.dto.request.TicketRequestDto;
import umc.tickettaka.web.dto.response.TicketResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams/{teamId}/projects/{projectId}/timelines/{timelineId}")
@Slf4j
public class TicketController {

    private final TicketCommandService ticketCommandService;
    private final TicketQueryService ticketQueryService;
    private final MemberCommandService memberCommandService;

    @GetMapping
    @Operation(summary = "Timeline별로 Ticket 조회")
    @Parameters({
        @Parameter(name = "teamId", description = "팀 아이디 : Path Variable"),
        @Parameter(name = "timelineId", description = "타임라인 아이디 : Path Variable"),
        @Parameter(name = "status", description = "티켓 상태 : todo, inprogress, done 셋 중 하나입니다.")
    })
    public ApiResponse<TicketResponseDto.ShowAllTicketListDto> showAllTickets(
        @AuthUser Member member,
        @PathVariable(name = "teamId") Long teamId,
        @PathVariable(name = "timelineId") Long timelineId,
        @RequestParam(name = "status") String status
    ) {
        return ApiResponse.onSuccess(ticketQueryService.getShowAllTicketListDto(member, teamId, timelineId, status));
    }

    //todo team controller로 이동
    @GetMapping("/member-info")
    @Operation(summary = "Ticket 생성창 조회")
    @Parameters({
        @Parameter(name = "teamId", description = "팀 아이디 : Path Variable")
    })
    public ApiResponse<CommonMemberDto.ShowMemberProfileListDto> createTicketPage(
        @PathVariable(name = "teamId") Long teamId
    ) {
        return ApiResponse.onSuccess(memberCommandService.getCommonMemberDto(teamId));
    }

    @PostMapping
    @Operation(summary = "Ticket 생성 요청")
    @Parameters({
        @Parameter(name = "timelineId", description = "타임라인 아이디 : Path Variable")
    })
    public ApiResponse<TicketResponseDto.CreateTicketResultDto> createTicket(
        @PathVariable(name = "timelineId") Long timelineId,
        @RequestBody TicketRequestDto.CreateTicketDto request
    ) throws IOException {
        Ticket ticket = ticketCommandService.createTicket(timelineId, request);
        return ApiResponse.onCreate(TicketConverter.toCreateTicketResultDto(ticket));
    }

    @DeleteMapping
    @Operation(summary = "Ticket 삭제")
    public ApiResponse<List<ShowTicketDto>> deleteTicket(
        @RequestBody TicketRequestDto.DeleteTicketDto request
    ) {

        ticketCommandService.deleteTicket(request);
        return ApiResponse.onSuccess(null);
    }
}
