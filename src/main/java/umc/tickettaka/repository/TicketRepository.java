package umc.tickettaka.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.Team;
import umc.tickettaka.domain.Timeline;
import umc.tickettaka.domain.ticket.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM Ticket t WHERE t.timeline.id = :timelineId ORDER BY t.endTime")
    @EntityGraph(attributePaths = "worker")
    List<Ticket> findAllByTimelineIdOrderByEndTime(Long timelineId);

    @EntityGraph(attributePaths = "worker")
    List<Ticket> findAllByWorker(Member member);

    Long countByTimeline(Timeline timeline);

    @Query("SELECT t from Ticket t where t.worker.id = :memberId order by t.endTime")
    @EntityGraph(attributePaths = "team")
    List<Ticket> findAllByWorkerOrderByEndTime(@Param("memberId") Long memberId);

    @EntityGraph(attributePaths = {"fileList", "worker"})
    List<Ticket> findAllByTeam(Team team);
}
