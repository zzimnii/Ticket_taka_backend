package umc.tickettaka.service;

import umc.tickettaka.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberQueryService {
    Member findByUsername(String username);

    Member findMemberWithTicketsAndMemberTeamsByMemberId(Long memberId);

    Optional<Member> findByProviderId(String providerId);
}
