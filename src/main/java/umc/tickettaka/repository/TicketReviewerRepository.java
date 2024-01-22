package umc.tickettaka.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import umc.tickettaka.domain.mapping.TicketReviewer;
import umc.tickettaka.domain.ticket.Ticket;

public interface TicketReviewerRepository extends JpaRepository<TicketReviewer, Long> {

    List<TicketReviewer> findAllByTicket(Ticket ticket);
}
