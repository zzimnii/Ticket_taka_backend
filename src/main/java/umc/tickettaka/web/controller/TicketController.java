package umc.tickettaka.web.controller;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import umc.tickettaka.converter.TicketConverter;
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

    @GetMapping("/create")
    public ApiResponse<CommonMemberDto.ShowMemberProfileListDto> createTicketPage(
        @PathVariable(name = "teamId") Long teamId
    ) {
        return ApiResponse.onSuccess(memberCommandService.getCommonMemberDto(teamId));
    }

    @PostMapping(value = "/create", consumes = {MediaType.APPLICATION_JSON_VALUE , MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<TicketResponseDto.CreateTicketResultDto> createTicket(
        @PathVariable(name = "timelineId") Long timelineId,
        @RequestPart(value = "files", required = false) List<MultipartFile> files,
        @RequestPart(value = "request") TicketRequestDto.CreateTicketDto request
    ) throws IOException {

        Ticket ticket = ticketCommandService.createTicket(timelineId, files, request);
        return ApiResponse.onCreate(TicketConverter.toCreateTicketResultDto(ticket));
    }

    @GetMapping("")
    public ApiResponse<TicketResponseDto.ShowAllTicketListDto> showAllTickets(
        @PathVariable(name = "teamId") Long teamId,
        @PathVariable(name = "timelineId") Long timelineId
    ) {
        return ApiResponse.onSuccess(ticketQueryService.getShowAllTicketListDto(teamId, timelineId));
    }

    @DeleteMapping("")
    public ApiResponse<List<ShowTicketDto>> deleteTicket(
        @RequestBody TicketRequestDto.DeleteTicketDto request
    ) {

        ticketCommandService.deleteTicket(request);
        return ApiResponse.onSuccess(null);
    }
}
