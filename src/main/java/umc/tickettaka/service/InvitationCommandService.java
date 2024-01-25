package umc.tickettaka.service;

import umc.tickettaka.domain.Invitation;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.Team;
import umc.tickettaka.web.dto.request.InvitationRequestDto;

import java.util.List;

public interface InvitationCommandService {
    List<Invitation> sendInvitation(Member sender, Team team, List<String> receiverUsername);
    void isAcceptedInvitation(Long id, Member receiver, InvitationRequestDto.AcceptInvitationDto request);
    void acceptInvitation(Long id, Member receiver);
    void rejectInvitation(Long id, Member receiver);
}
