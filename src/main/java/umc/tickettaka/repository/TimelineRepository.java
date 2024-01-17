package umc.tickettaka.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import umc.tickettaka.domain.Timeline;

public interface TimelineRepository extends JpaRepository<Timeline, Long> {

    List<Timeline> findAllByProjectId(Long projectId);
}
