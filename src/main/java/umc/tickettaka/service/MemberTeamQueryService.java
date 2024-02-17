package umc.tickettaka.service;

import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.Team;
import umc.tickettaka.domain.mapping.MemberTeam;

import java.util.List;

public interface MemberTeamQueryService {
    List<MemberTeam> findAllMembersByTeam(Team team);

    MemberTeam findByTeamAndMember(Team team, Member member);

    List<MemberTeam> findAllByMember(Member member);
}