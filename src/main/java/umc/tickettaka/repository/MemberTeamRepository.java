package umc.tickettaka.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.Team;
import umc.tickettaka.domain.mapping.MemberTeam;

import java.util.List;
import java.util.Optional;

public interface MemberTeamRepository extends JpaRepository<MemberTeam, Long> {
    Optional<MemberTeam> findByTeamAndMember(Team team, Member member);

    @EntityGraph(attributePaths = {"team", "member.ticketList"})
    List<MemberTeam> findAllMembersByTeam(Team team);

    @EntityGraph(attributePaths = {"team", "member.ticketList"})
    List<MemberTeam> findAllByMember(Member member);
}