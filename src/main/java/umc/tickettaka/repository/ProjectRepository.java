package umc.tickettaka.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import umc.tickettaka.domain.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByTeamId(Long teamId);
}
