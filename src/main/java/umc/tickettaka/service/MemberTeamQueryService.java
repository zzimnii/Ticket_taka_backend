package umc.tickettaka.service;

import umc.tickettaka.domain.Team;
import umc.tickettaka.domain.mapping.MemberTeam;

import java.util.List;

public interface MemberTeamQueryService {
    List<MemberTeam> findAllMembersByTeam(Team team);
}