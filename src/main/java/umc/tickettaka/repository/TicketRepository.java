package umc.tickettaka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.tickettaka.domain.ticket.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
