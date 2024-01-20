package umc.tickettaka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.tickettaka.domain.mapping.TicketReviewer;

public interface TicketReviewerRepository extends JpaRepository<TicketReviewer, Long> {

}
