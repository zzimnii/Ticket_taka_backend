package umc.tickettaka.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.tickettaka.domain.Invitation;
import umc.tickettaka.domain.Member;
import umc.tickettaka.payload.exception.GeneralException;
import umc.tickettaka.payload.status.ErrorStatus;
import umc.tickettaka.repository.InvitationRepository;
import umc.tickettaka.service.InvitationQueryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InvitationQueryServiceImpl implements InvitationQueryService {

    private final InvitationRepository invitationRepository;

    @Override
    public Invitation findInvitation(Long id) {
        return invitationRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.INVITATION_NOT_FOUND));
    }

    @Override
    public List<Invitation> findAll() { return invitationRepository.findAll();}
}