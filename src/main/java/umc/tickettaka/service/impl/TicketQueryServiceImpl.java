package umc.tickettaka.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.tickettaka.converter.TicketConverter;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.ticket.Ticket;
import umc.tickettaka.payload.exception.GeneralException;
import umc.tickettaka.payload.status.ErrorStatus;
import umc.tickettaka.repository.TicketRepository;
import umc.tickettaka.service.TicketQueryService;
import umc.tickettaka.web.dto.response.CommonTicketDto;

@Service
@RequiredArgsConstructor
public class TicketQueryServiceImpl implements TicketQueryService {

    private final TicketRepository ticketRepository;

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
    public List<CommonTicketDto> getCommonTicketDto(Member member) {
        List<Ticket> ticketList = findAllByWorker(member);
        return TicketConverter.toCommonTicketDtoList(ticketList);
    }

    @Override
    public List<CommonTicketDto> getCommonTicketDto(Long timelineId) {
        List<Ticket> ticketList = findAllByTimelineId(timelineId);
        return TicketConverter.toCommonTicketDtoList(ticketList);
    }
}
