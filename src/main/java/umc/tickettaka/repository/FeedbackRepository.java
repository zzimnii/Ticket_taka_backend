package umc.tickettaka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.tickettaka.domain.ticket.Feedback;
import umc.tickettaka.domain.ticket.Ticket;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Feedback findByTicket(Ticket ticket);
}
