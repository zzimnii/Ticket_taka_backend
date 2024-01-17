package umc.tickettaka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.tickettaka.domain.mapping.MemberTeam;

public interface MemberTeamRepository extends JpaRepository<MemberTeam, Long> {

}
