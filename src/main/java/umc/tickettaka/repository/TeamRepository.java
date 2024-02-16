package umc.tickettaka.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.Project;
import umc.tickettaka.domain.Team;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByMemberTeamListMember(Member member);

    @Query("SELECT t FROM Team t "
        + "LEFT JOIN t.projectList p "
        + "LEFT JOIN p.timelineList tl "
        + "WHERE tl.id = :timelineId and p = :project")
    Optional<Team> findTeamByProjectAndTimeline(@Param("project") Project project, @Param("timelineId") Long timelineId);

}