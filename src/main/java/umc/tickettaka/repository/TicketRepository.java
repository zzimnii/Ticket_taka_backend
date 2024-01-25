package umc.tickettaka.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.Timeline;
import umc.tickettaka.domain.ticket.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM Ticket t LEFT JOIN FETCH t.fileList f WHERE t.timeline.id = :timelineId ORDER BY t.endTime")
    @EntityGraph(attributePaths = "worker")
    List<Ticket> findAllByTimelineIdOrderByEndTime(Long timelineId);

    @EntityGraph(attributePaths = "worker")
    List<Ticket> findAllByWorker(Member member);

    Long countByTimeline(Timeline timeline);
}
