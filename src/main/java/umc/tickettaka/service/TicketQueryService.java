package umc.tickettaka.service;

import java.util.List;
import umc.tickettaka.domain.ticket.Ticket;

public interface TicketQueryService {

    List<Ticket> findAllByTimelineId(Long timelineId);
    Ticket findById(Long id);

}
