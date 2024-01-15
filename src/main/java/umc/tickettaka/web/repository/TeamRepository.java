package umc.tickettaka.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.tickettaka.domain.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}