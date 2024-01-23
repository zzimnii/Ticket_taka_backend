package umc.tickettaka.converter;

import java.time.LocalDate;
import java.util.List;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.Timeline;
import umc.tickettaka.domain.enums.TicketStatus;
import umc.tickettaka.domain.ticket.File;
import umc.tickettaka.domain.ticket.Ticket;
import umc.tickettaka.payload.exception.GeneralException;
import umc.tickettaka.payload.status.ErrorStatus;
import umc.tickettaka.web.dto.request.TicketRequestDto.CreateTicketDto;
import umc.tickettaka.web.dto.response.CommonTicketDto;
import umc.tickettaka.web.dto.response.TicketResponseDto;
import umc.tickettaka.web.dto.response.TicketResponseDto.CreateTicketResultDto;

public class TicketConverter {

    public static TicketResponseDto.CreateTicketResultDto toCreateTicketResultDto(Ticket ticket) {
        return CreateTicketResultDto.builder()
            .id(ticket.getId())
            .sequence(ticket.getSequence())
            .createdTime(ticket.getCreatedTime())
            .build();
    }

    public static Ticket toTicket(Timeline timeline, Member worker, Long sequence, CreateTicketDto request) {
        LocalDate startTime = LocalDate.of(request.getStartYear(), request.getStartMonth(), request.getStartDay());
        LocalDate endTime = LocalDate.of(request.getEndYear(), request.getEndMonth(), request.getEndDay());
        if (startTime.isAfter(endTime)) {
            throw new GeneralException(ErrorStatus.INVALID_TICKET_TIME);
        }

        return Ticket.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .sequence(sequence)
            .worker(worker)
            .status(TicketStatus.TODO)
            .timeline(timeline)
            .startTime(startTime)
            .endTime(endTime)
            .build();
        //todo next ticket?
    }

    public static List<CommonTicketDto> toCommonTicketDtoList(List<Ticket> ticketList) {


        return ticketList.stream()
            .map(ticket -> CommonTicketDto.builder()
                .ticketId(ticket.getId())
                .sequence(ticket.getSequence())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .fileUrlList(getFileUrlList(ticket.getFileList()))
                .status(String.valueOf(ticket.getStatus()))
                .endTime(ticket.getEndTime())
                .build()
            ).toList();
    }

    private static List<String> getFileUrlList(List<File> fileList) {
        return fileList.stream()
            .map(File::getUrl).toList();
    }
}