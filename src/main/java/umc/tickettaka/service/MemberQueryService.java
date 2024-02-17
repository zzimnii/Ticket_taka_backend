package umc.tickettaka.service;

import umc.tickettaka.domain.Member;

import java.util.Optional;
import umc.tickettaka.web.dto.response.MemberResponseDto.MyPageDto;

public interface MemberQueryService {
    Member findByUsername(String username);

    MyPageDto getMyPageDto(Member member, Long teamId, String status);

    Optional<Member> findByEmail(String email);
    Optional<Member> findByEmailAndProvider(String email, String provider);
}
