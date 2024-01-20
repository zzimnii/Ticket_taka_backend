package umc.tickettaka.converter;

import java.time.LocalDate;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.Timeline;
import umc.tickettaka.domain.enums.TicketStatus;
import umc.tickettaka.domain.ticket.Ticket;
import umc.tickettaka.payload.exception.GeneralException;
import umc.tickettaka.payload.status.ErrorStatus;
import umc.tickettaka.web.dto.request.TicketRequestDto.CreateTicketDto;
import umc.tickettaka.web.dto.response.TicketResponseDto;
import umc.tickettaka.web.dto.response.TicketResponseDto.CreateTicketResultDto;

public class TicketConverter {

    public static TicketResponseDto.CreateTicketResultDto toCreateTicketResultDto(Ticket ticket) {
        return CreateTicketResultDto.builder()
            .id(ticket.getId())
            .createdTime(ticket.getCreatedTime())
            .build();
    }

    public static Ticket toTicket(Timeline timeline, CreateTicketDto request, Member worker) {
        LocalDate startTime = LocalDate.of(request.getStartYear(), request.getStartMonth(), request.getStartDay());
        LocalDate endTime = LocalDate.of(request.getEndYear(), request.getEndMonth(), request.getEndDay());
        if (startTime.isAfter(endTime)) {
            throw new GeneralException(ErrorStatus.INVALID_TICKET_TIME);
        }

        return Ticket.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .worker(worker)
            .status(TicketStatus.TODO)
            .timeline(timeline)
            .startTime(startTime)
            .endTime(endTime)
            .build();
        //todo next ticket?
    }
}
