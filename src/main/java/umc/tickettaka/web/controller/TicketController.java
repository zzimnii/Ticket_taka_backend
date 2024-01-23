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
import umc.tickettaka.service.TicketCommandService;
import umc.tickettaka.service.TicketQueryService;
import umc.tickettaka.web.dto.request.TicketRequestDto;
import umc.tickettaka.web.dto.response.CommonTicketDto;
import umc.tickettaka.web.dto.response.TicketResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams/{teamsId}/projects/{projectId}/timelines/{timelineId}")
@Slf4j
public class TicketController {

    private final TicketCommandService ticketCommandService;
    private final TicketQueryService ticketQueryService;

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
    public ApiResponse<List<CommonTicketDto>> showAllTickets(
        @PathVariable(name = "timelineId") Long timelineId
    ) {
        return ApiResponse.onSuccess(ticketQueryService.getCommonTicketDto(timelineId));
    }

    @DeleteMapping("")
    public ApiResponse<List<CommonTicketDto>> deleteTicket(
        @RequestBody TicketRequestDto.DeleteTicketDto request
    ) {

        ticketCommandService.deleteTicket(request);
        return ApiResponse.onSuccess(null);
    }
}
