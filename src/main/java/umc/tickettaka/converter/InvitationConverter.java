package umc.tickettaka.converter;

import umc.tickettaka.domain.Invitation;
import umc.tickettaka.domain.Member;
import umc.tickettaka.web.dto.response.InvitationResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class InvitationConverter {

    public static InvitationResponseDto.InvitationDto invitationDto(Invitation invitation) {
        return InvitationResponseDto.InvitationDto.builder()
                .id(invitation.getId())
                .teamsId(invitation.getTeam().getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static InvitationResponseDto.InvitationListDto toInvitationListDto(Member member, List<Invitation> invitationList) {
        List<InvitationResponseDto.InvitationDto> invitationDtoList = invitationList.stream()
                .filter(invitation -> member.getId().equals(invitation.getReceiver().getId()))
                .map(InvitationConverter::invitationDto)
                .collect(Collectors.toList());

        return InvitationResponseDto.InvitationListDto.builder()
                .invitationDtoList(invitationDtoList)
                .build();
    }
}