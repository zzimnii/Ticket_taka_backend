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
import umc.tickettaka.web.dto.common.CommonMemberDto;
import umc.tickettaka.web.dto.common.CommonMemberDto.ShowMemberProfileListDto;
import umc.tickettaka.web.dto.common.CommonTicketDto.ShowTicketDto;
import umc.tickettaka.web.dto.request.TicketRequestDto.CreateTicketDto;
import umc.tickettaka.web.dto.response.TicketResponseDto;
import umc.tickettaka.web.dto.response.TicketResponseDto.CreateTicketResultDto;
import umc.tickettaka.web.dto.response.TicketResponseDto.ShowAllTicketListDto;

public class TicketConverter {

    public static TicketResponseDto.CreateTicketResultDto toCreateTicketResultDto(Ticket ticket) {
        return CreateTicketResultDto.builder()
            .id(ticket.getId())
            .sequence(ticket.getSequence())
            .createdTime(ticket.getCreatedTime())
            .build();
    }

    public static Ticket toTicket(Timeline timeline, Member worker, Long sequence, CreateTicketDto request) {

        LocalDate startTime = LocalDate.parse(request.getStartTime());
        LocalDate endTime = LocalDate.parse(request.getEndTime());
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

    public static List<ShowTicketDto> toShowTicketDtoList(Member member, List<Ticket> ticketList) {


        return ticketList.stream()
            .map(ticket -> ShowTicketDto.builder()
                .ticketId(ticket.getId())
                .workerName(ticket.getWorker().getUsername())
                .sequence(ticket.getSequence())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .fileUrlList(getFileUrlList(ticket.getFileList()))
                .status(String.valueOf(ticket.getStatus()))
                .endTime(ticket.getEndTime())
                //todo nextTicket
                .nextTicket(null)
                .isMyTicket(ticket.getWorker().getUsername().equals(member.getUsername()))
                .build()
            ).toList();
    }

    private static List<String> getFileUrlList(List<File> fileList) {
        return fileList.stream()
            .map(File::getUrl).toList();
    }

    public static ShowAllTicketListDto toShowAllTicketListDto(
        String timelineName,
        ShowMemberProfileListDto memberProfileListDto,
        List<ShowTicketDto> ticketDtoList) {

        return ShowAllTicketListDto.builder()
            .timelineName(timelineName)
            .ticketDtoList(ticketDtoList)
            .memberProfileListDto(memberProfileListDto)
            .build();
    }

    public static TicketResponseDto.MemberAchieveLevelDto toMemberAchieveLevelDto (
            CommonMemberDto.ShowMemberProfileDto memberProfileDto,
            int totalTicket,
            int doneTicket) {

        return TicketResponseDto.MemberAchieveLevelDto.builder()
                .profileDto(memberProfileDto)
                .totalTicket(totalTicket)
                .doneTicket(doneTicket)
                .build();
    }

    public static TicketStatus toTicketStatus(String status) {
        switch (status.toLowerCase()) {
            case "todo":
                return TicketStatus.TODO;
            case "inprogress":
                return TicketStatus.IN_PROGRESS;
            case "done":
                return TicketStatus.DONE;
            default:
                throw new GeneralException(ErrorStatus.BAD_REQUEST);
        }
    }
}
