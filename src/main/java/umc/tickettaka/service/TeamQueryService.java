package umc.tickettaka.service;

import java.util.List;

import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.Project;
import umc.tickettaka.domain.Team;

public interface TeamQueryService {
    Team findTeam(Long id);
    Team findTeamByProjectAndTimeline(Project project, Long timelineId);
    List<Team> findAll();
    List<Team> findTeamsByMember(Member member);
}