package umc.tickettaka.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.tickettaka.converter.MemberConverter;
import umc.tickettaka.converter.TeamConverter;
import umc.tickettaka.converter.TicketConverter;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.Team;
import umc.tickettaka.domain.enums.ProviderType;
import umc.tickettaka.domain.enums.TicketStatus;
import umc.tickettaka.domain.ticket.Ticket;
import umc.tickettaka.payload.exception.GeneralException;
import umc.tickettaka.payload.status.ErrorStatus;
import umc.tickettaka.repository.MemberRepository;
import umc.tickettaka.repository.TicketRepository;
import umc.tickettaka.service.MemberQueryService;
import umc.tickettaka.service.TeamQueryService;
import umc.tickettaka.web.dto.common.CommonMemberDto.ShowMemberProfileDto;
import umc.tickettaka.web.dto.common.CommonTicketDto.ShowTicketDto;

import java.util.List;
import java.util.Optional;
import umc.tickettaka.web.dto.response.MemberResponseDto.MyPageDto;
import umc.tickettaka.web.dto.response.TeamResponseDto.TeamSelectDto;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberRepository memberRepository;
    private final TeamQueryService teamQueryService;
    private final TicketRepository ticketRepository;

    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username)
            .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND, "해당 username을 가진 회원이 없습니다."));
    }

    @Override
    public MyPageDto getMyPageDto(Member member, Long teamId, String status) {
        List<Team> teamList = teamQueryService.findTeamsByMember(member);
        List<TeamSelectDto> teamSelectDtoList = TeamConverter.toTeamSelectDto(teamList);

        ShowMemberProfileDto profileDto = MemberConverter.toMemberProfileDto(member);

        List<Ticket> ticketList = ticketRepository.findAllByWorker(member);

        if (teamId != null) {
            ticketList = ticketList.stream()
                .filter(ticket -> ticket.getTeam().getId().equals(teamId))
                .toList();
        }
        if (status != null) {
            TicketStatus ticketStatus = TicketConverter.toTicketStatus(status);
            ticketList = ticketList.stream()
                .filter(ticket -> ticket.getStatus().equals(ticketStatus))
                .toList();
        }

        List<ShowTicketDto> showTicketDtoList = TicketConverter.toShowTicketDtoList(member, ticketList);

        return MyPageDto.builder()
            .profileDto(profileDto)
            .showTicketDtoList(showTicketDtoList)
            .teamSelectDtoList(teamSelectDtoList)
            .build();

//            Member memberWithTickets = memberRepository.findMemberWithTicketsById(memberId)
//                    .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND, "member를 찾을 수 없습니다."));
//
//            Member memberWithMemberTeams = memberRepository.findMemberWitMemberTeamById(memberId)
//                    .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND, "member를 찾을 수 없습니다."));
//
//            List<Ticket> tickets = ticketRepository.findAllByWorkerOrderByEndTime(memberWithMemberTeams);
//
//            return memberWithTickets;

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
