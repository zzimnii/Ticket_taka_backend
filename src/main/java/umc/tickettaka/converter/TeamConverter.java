package umc.tickettaka.converter;

import umc.tickettaka.domain.Invitation;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.Team;
import umc.tickettaka.web.dto.request.TeamRequestDto;
import umc.tickettaka.web.dto.response.InvitationResponseDto;
import umc.tickettaka.web.dto.response.TeamResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TeamConverter {

    public static TeamResponseDto.TeamDto toTeamResultDto(Team team) {
        return TeamResponseDto.TeamDto.builder()
                .teamId(team.getId())
                .name(team.getName())
                .imageUrl(team.getImageUrl())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Team toTeam(TeamRequestDto.CreateTeamDto request, String imageUrl) {
        return Team.builder()
                .name(request.getName())
                .imageUrl(imageUrl)
                .build();
    }

    public static TeamResponseDto.TeamAndInvitationListDto teamAndInvitationListDto(Member member, List<Team> teamList, List<Invitation> invitationList) {
        List<TeamResponseDto.TeamDto> teamDtoList = teamList.stream()
                .map(TeamConverter::toTeamResultDto)
                .collect(Collectors.toList());

        List<InvitationResponseDto.InvitationDto> invitationDtoList = invitationList.stream()
                .filter(invitation -> member.getId().equals(invitation.getReceiver().getId()))
                .map(InvitationConverter::invitationDto)
                .toList();

        return TeamResponseDto.TeamAndInvitationListDto.builder()
                .teamDtoList(teamDtoList)
                .invitationDtoList(invitationDtoList)
                .build();
    }
}
