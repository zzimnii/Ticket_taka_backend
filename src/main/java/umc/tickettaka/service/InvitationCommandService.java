package umc.tickettaka.service;

import umc.tickettaka.domain.Invitation;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.Team;

public interface InvitationCommandService {
    Invitation sendInvitation(Member sender, Team team, String receiverUsername);
    void acceptInvitation(Long id, Member receiver);
    void rejectInvitation(Long id, Member receiver);
}
