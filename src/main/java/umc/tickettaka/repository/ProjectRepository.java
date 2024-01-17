package umc.tickettaka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.tickettaka.domain.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}
