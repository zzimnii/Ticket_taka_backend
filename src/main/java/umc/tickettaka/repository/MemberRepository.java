package umc.tickettaka.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import umc.tickettaka.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String userName);

    Optional<Member> findByProviderId(String providerId);


    @EntityGraph(attributePaths = {"ticketList"})
    Optional<Member> findMemberWithTicketsById(Long memberId);

    @EntityGraph(attributePaths = {"memberTeamList", "memberTeamList.team"})
    Optional<Member> findMemberWitMemberTeamById(Long memberId);

}
