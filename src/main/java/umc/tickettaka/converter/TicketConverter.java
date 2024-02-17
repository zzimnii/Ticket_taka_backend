package umc.tickettaka.converter;

import java.time.LocalDate;
import java.util.List;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.Team;
import umc.tickettaka.domain.Timeline;
import umc.tickettaka.domain.enums.TicketStatus;
import umc.tickettaka.domain.mapping.MemberTeam;
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

    public static Ticket toTicket(Timeline timeline, Team team, Member worker, Long sequence, MemberTeam memberTeam, CreateTicketDto request) {

        LocalDate startTime = LocalDate.parse(request.getStartTime());
        LocalDate endTime = LocalDate.parse(request.getEndTime());
        if (startTime.isAfter(endTime)) {
            throw new GeneralException(ErrorStatus.INVALID_TICKET_TIME);
        }
        if (startTime.isBefore(LocalDate.now())) {
            throw new GeneralException(ErrorStatus.TICKET_TIME_ERROR);
        }

        return Ticket.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .sequence(sequence)
            .worker(worker)
            .team(team)
            .workerTeam(memberTeam)
            .status(TicketStatus.TODO)
            .timeline(timeline)
            .fileList(request.getFileUrlList())
            .startTime(startTime)
            .endTime(endTime)
            .build();
    }

    public static List<ShowTicketDto> toShowTicketDtoList(Member member, List<Ticket> ticketList) {


        return ticketList.stream()
            .map(ticket -> ShowTicketDto.builder()
                .ticketId(ticket.getId())
                .ticketHex(ticket.getWorkerTeam().getColor().getHex())
                .sequence(ticket.getSequence())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .fileUrlList(ticket.getFileList())
                .status(String.valueOf(ticket.getStatus()))
                .startTime(ticket.getStartTime())
                .endTime(ticket.getEndTime())
                .isMyTicket(ticket.getWorker().getUsername().equals(member.getUsername()))
                .build()
            ).toList();
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
                .achieveLevel((double)doneTicket/totalTicket)
                .build();
    }

    public static TicketStatus toTicketStatus(String status) {
        return switch (status.toLowerCase()) {
            case "todo" -> TicketStatus.TODO;
            case "inprogress" -> TicketStatus.IN_PROGRESS;
            case "done" -> TicketStatus.DONE;
            default -> throw new GeneralException(ErrorStatus.BAD_REQUEST);
        };
    }
}
