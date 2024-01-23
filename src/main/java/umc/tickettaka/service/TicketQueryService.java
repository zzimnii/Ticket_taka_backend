package umc.tickettaka.service;

import java.util.List;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.ticket.Ticket;
import umc.tickettaka.web.dto.response.CommonTicketDto;

public interface TicketQueryService {

    List<Ticket> findAllByTimelineId(Long timelineId);
    Ticket findById(Long id);
    List<Ticket> findAllByWorker(Member member);

    List<CommonTicketDto> getCommonTicketDto(Member member);
    List<CommonTicketDto> getCommonTicketDto(Long timelineId);
}
