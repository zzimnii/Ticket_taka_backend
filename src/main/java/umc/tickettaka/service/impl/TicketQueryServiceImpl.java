package umc.tickettaka.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import umc.tickettaka.converter.TicketConverter;
import umc.tickettaka.domain.Member;
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
        return TicketConverter.toShowTicketDtoList(ticketList);
    }

    @Override
    public List<ShowTicketDto> getShowTicketDto(Long timelineId) {
        List<Ticket> ticketList = findAllByTimelineId(timelineId);
        return TicketConverter.toShowTicketDtoList(ticketList);
    }

    @Override
    public ShowAllTicketListDto getShowAllTicketListDto(Long teamId, Long timelineId) {
        List<ShowTicketDto> ticketDtoList = getShowTicketDto(timelineId);
        log.info("getShowTicketDto");
        String timelineName = timelineQueryService.findById(timelineId).getName();
        ShowMemberProfileListDto memberProfileListDto = memberCommandService.getCommonMemberDto(teamId);

        return TicketConverter.toShowAllTicketListDto(timelineName, memberProfileListDto, ticketDtoList);
    }
}
