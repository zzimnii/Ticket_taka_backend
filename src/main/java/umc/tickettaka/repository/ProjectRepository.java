package umc.tickettaka.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.tickettaka.domain.Project;
import umc.tickettaka.domain.Timeline;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByTeamId(Long teamId);

    @Query("select p from Project p join fetch p.timelineList tl where tl = :timeline")
    Optional<Project> findByTimeline(@Param("timeline") Timeline timeline);
}
