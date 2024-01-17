package umc.tickettaka.service;

import umc.tickettaka.domain.Member;

import java.util.Optional;

public interface MemberQueryService {
    Member findByEmail(String email);
    Member findByUsername(String username);

    Optional<Member> findByProviderId(String providerId);
}
