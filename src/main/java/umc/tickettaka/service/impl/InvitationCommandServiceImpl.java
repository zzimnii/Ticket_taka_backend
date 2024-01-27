package umc.tickettaka.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.tickettaka.domain.Invitation;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.Team;
import umc.tickettaka.domain.enums.Color;
import umc.tickettaka.domain.mapping.MemberTeam;
import umc.tickettaka.payload.exception.GeneralException;
import umc.tickettaka.payload.status.ErrorStatus;
import umc.tickettaka.repository.InvitationRepository;
import umc.tickettaka.repository.MemberTeamRepository;
import umc.tickettaka.service.*;
import umc.tickettaka.web.dto.request.InvitationRequestDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvitationCommandServiceImpl implements InvitationCommandService {

    private final InvitationRepository invitationRepository;
    private final InvitationQueryService invitationQueryService;
    private final TeamCommandServiceImpl teamCommandService;
    private final TeamQueryService teamQueryService;
    private final MemberTeamRepository memberTeamRepository;
    private final MemberQueryService memberQueryService;

    @Override
    @Transactional
    public List<Invitation> sendInvitation(Member sender, Team team, List<String> receiverUsernames) {
        return receiverUsernames.stream()
                .map(receiverUsername -> {
                    Member receiver = memberQueryService.findByUsername(receiverUsername);

                    Optional<MemberTeam> existingMemberTeam = memberTeamRepository.findByTeamAndMember(team, receiver);
                    Optional<Invitation> invitationOptional = invitationRepository.findByReceiverAndTeam(receiver, team);

                    existingMemberTeam.ifPresent(memberTeam -> {
                        throw new GeneralException(ErrorStatus.MEMBER_TEAM_ALREADY_EXIST);
                    });
                    invitationOptional.ifPresent(invitation -> {
                        throw new GeneralException(ErrorStatus.INVITATION_ALREADY_EXIST);
                    });

                    return Invitation.builder()
                            .sender(sender)
                            .receiver(receiver)
                            .team(team)
                            .build();
                })
                .map(invitationRepository::save)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void isAcceptedInvitation(Long invitationId, Member receiver, InvitationRequestDto.AcceptInvitationDto request) {

        if (request.getAccept()) {
            acceptInvitation(invitationId, receiver);
        } else {
            rejectInvitation(invitationId, receiver);
        }
    }

    @Override
    public void acceptInvitation(Long id, Member receiver) {
        Invitation invitation = invitationQueryService.findInvitation(id);
        Team team = teamQueryService.findTeam(invitation.getTeam().getId());

        if (receiver.getId().equals(invitation.getReceiver().getId())) {
            Optional<MemberTeam> existingMemberTeam = memberTeamRepository.findByTeamAndMember(team, receiver);
            Color newColor;

            if (existingMemberTeam.isPresent()) {
                throw new GeneralException(ErrorStatus.MEMBER_TEAM_ALREADY_EXIST);
            }

            do {
                newColor = Color.getRandomColor();
            } while (teamCommandService.isColorAlreadyUsedInTeam(team, newColor));

            List<MemberTeam> memberTeamList = new ArrayList<>();

            MemberTeam newMemberTeam = MemberTeam.builder()
                    .team(team)
                    .color(newColor)
                    .member(receiver)
                    .build();

            memberTeamList.add(newMemberTeam);
            memberTeamRepository.saveAll(memberTeamList);
            invitationRepository.delete(invitation);
        } else {
            throw new GeneralException(ErrorStatus.UNAUTHORIZED_ACCESS);
        }
    }

    @Override
    @Transactional
    public void rejectInvitation(Long id, Member receiver) {
        Invitation invitation = invitationQueryService.findInvitation(id);

        if (receiver.getId().equals(invitation.getReceiver().getId())) {
            invitationRepository.delete(invitation);
        } else {
            throw new GeneralException(ErrorStatus.UNAUTHORIZED_ACCESS);
        }
    }
}