package umc.tickettaka.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.tickettaka.converter.MemberConverter;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.enums.ProviderType;
import umc.tickettaka.domain.ticket.Ticket;
import umc.tickettaka.payload.exception.GeneralException;
import umc.tickettaka.payload.status.ErrorStatus;
import umc.tickettaka.repository.MemberRepository;
import umc.tickettaka.repository.TicketRepository;
import umc.tickettaka.service.MemberQueryService;
import umc.tickettaka.web.dto.response.MemberResponseDto;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberRepository memberRepository;
    private final TicketRepository ticketRepository;

    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username)
            .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND, "해당 username을 가진 회원이 없습니다."));
    }

    @Override
    public Member findMemberWithTicketsAndMemberTeamsByMemberId(Long memberId) {
//        Member member = memberRepository.findMemberWithTicketsAndFilesOrderByEndTime(memberId)
//                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND, "member를 찾을 수 없습니다."));

        Member memberWithTickets = memberRepository.findMemberWithTicketsById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND, "member를 찾을 수 없습니다."));

        Member memberWithMemberTeams = memberRepository.findMemberWitMemberTeamById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND, "member를 찾을 수 없습니다."));

        List<Ticket> tickets = ticketRepository.findAllWithFileByWorkerOrderByEndTime(memberWithMemberTeams.getId());

        return memberWithTickets;

    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Override
    public Optional<Member> findByEmailAndProvider(String email, String provider) {
        return memberRepository.findByEmailAndProviderType(email, ProviderType.valueOf(provider));
    }


}
