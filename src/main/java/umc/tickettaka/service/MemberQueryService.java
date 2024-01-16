package umc.tickettaka.service;

import umc.tickettaka.domain.Member;

public interface MemberQueryService {
    Member findByEmail(String email);
    Member findByUsername(String username);
}
