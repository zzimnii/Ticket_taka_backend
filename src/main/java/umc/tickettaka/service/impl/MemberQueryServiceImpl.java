package umc.tickettaka.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.tickettaka.domain.Member;
import umc.tickettaka.payload.exception.GeneralException;
import umc.tickettaka.payload.status.ErrorStatus;
import umc.tickettaka.repository.MemberRepository;
import umc.tickettaka.service.MemberQueryService;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberRepository memberRepository;

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND, "해당 email을 가진 회원이 없습니다."));

    }

    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username)
            .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND, "해당 username을 가진 회원이 없습니다."));
    }
}
