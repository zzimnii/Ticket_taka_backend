package umc.tickettaka.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.enums.ProviderType;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    @EntityGraph(attributePaths = {"ticketList"})
    Optional<Member> findMemberWithTicketsById(Long memberId);

    @EntityGraph(attributePaths = {"memberTeamList", "memberTeamList.team"})
    Optional<Member> findMemberWitMemberTeamById(Long memberId);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByEmailAndProviderType(String email, ProviderType providerType);
}
