package umc.tickettaka.service.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.tickettaka.converter.TicketConverter;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.enums.TicketStatus;
import umc.tickettaka.domain.ticket.Ticket;
import umc.tickettaka.payload.exception.GeneralException;
import umc.tickettaka.payload.status.ErrorStatus;
import umc.tickettaka.repository.TicketRepository;
import umc.tickettaka.service.MemberCommandService;
import umc.tickettaka.service.TicketQueryService;
import umc.tickettaka.service.TimelineQueryService;
import umc.tickettaka.web.dto.common.CommonMemberDto.ShowMemberProfileListDto;
import umc.tickettaka.web.dto.common.CommonTicketDto.ShowTicketDto;
import umc.tickettaka.web.dto.response.TicketResponseDto.ShowAllTicketListDto;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TicketQueryServiceImpl implements TicketQueryService {

    private final TicketRepository ticketRepository;
    private final MemberCommandService memberCommandService;
    private final TimelineQueryService timelineQueryService;

    @Override
    public List<Ticket> findAllByTimelineId(Long timelineId) {
        return ticketRepository.findAllByTimelineIdOrderByEndTime(timelineId);
    }

    @Override
    public Ticket findById(Long id) {
        return ticketRepository.findById(id)
            .orElseThrow(() -> new GeneralException(ErrorStatus.TICKET_NOT_FOUND));
    }

    @Override
    public List<Ticket> findAllByWorker(Member member) {
        return ticketRepository.findAllByWorker(member);
    }

    @Override
    public List<ShowTicketDto> getShowTicketDto(Member member) {
        List<Ticket> ticketList = findAllByWorker(member);
        return TicketConverter.toShowTicketDtoList(member, ticketList);
    }

    @Override
    public List<ShowTicketDto> getShowTicketDto(Member member, Long timelineId) {
        List<Ticket> ticketList = findAllByTimelineId(timelineId);
        return TicketConverter.toShowTicketDtoList(member, ticketList);
    }

    @Override
    public List<ShowTicketDto> getShowTicketDto(Member member, Long timelineId, String statusStr) {
        List<Ticket> ticketList = findAllByTimelineId(timelineId);
        List<Ticket> ticketsByStatus = new ArrayList<>();
        TicketStatus status = TicketConverter.toTicketStatus(statusStr);
        for (Ticket ticket : ticketList) {
            if (status.equals(ticket.getStatus())) {
                ticketsByStatus.add(ticket);
            }
        }
        return TicketConverter.toShowTicketDtoList(member, ticketsByStatus);
    }

    @Override
    public ShowAllTicketListDto getShowAllTicketListDto(Member member, Long teamId, Long timelineId) {
        List<ShowTicketDto> ticketDtoList = getShowTicketDto(member, timelineId);
        String timelineName = timelineQueryService.findById(timelineId).getName();
        ShowMemberProfileListDto memberProfileListDto = memberCommandService.getCommonMemberDto(teamId);

        return TicketConverter.toShowAllTicketListDto(timelineName, memberProfileListDto, ticketDtoList);
    }

    @Override
    public ShowAllTicketListDto getShowAllTicketListDto(Member member, Long teamId, Long timelineId, String status) {
        List<ShowTicketDto> ticketDtoList = getShowTicketDto(member, timelineId, status);
        String timelineName = timelineQueryService.findById(timelineId).getName();
        ShowMemberProfileListDto memberProfileListDto = memberCommandService.getCommonMemberDto(teamId);

        return TicketConverter.toShowAllTicketListDto(timelineName, memberProfileListDto, ticketDtoList);
    }
}
