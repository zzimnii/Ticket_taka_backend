package umc.tickettaka.service;

import umc.tickettaka.domain.Invitation;

import java.util.List;

public interface InvitationQueryService {
    Invitation findInvitation(Long id);
    List<Invitation> findAll();
}
