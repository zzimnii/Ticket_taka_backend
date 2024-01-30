package umc.tickettaka.converter;

import umc.tickettaka.domain.Invitation;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.Team;
import umc.tickettaka.domain.enums.Color;
import umc.tickettaka.domain.mapping.MemberTeam;
import umc.tickettaka.domain.ticket.Ticket;
import umc.tickettaka.web.dto.common.CommonTicketDto.ShowTicketDto;
import umc.tickettaka.web.dto.request.TeamRequestDto;
import umc.tickettaka.web.dto.response.InvitationResponseDto;
import umc.tickettaka.web.dto.response.TeamResponseDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TeamConverter {

    public static TeamResponseDto.TeamDto toTeamResultDto(Team team) {
        return TeamResponseDto.TeamDto.builder()
                .teamId(team.getId())
                .teamName(team.getName())
                .imageUrl(team.getImageUrl())
                .build();
    }

    public static Team toTeam(TeamRequestDto.CreateTeamDto request, String imageUrl) {
        return Team.builder()
                .name(request.getTeamName())
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

    public static TeamResponseDto.TeamCalendarDto teamCalendarDto(List<MemberTeam> memberTeams, Team team, List<Ticket> ticketList) {
        Map<Long, Color> colorMap = new HashMap<>();
        List<TeamResponseDto.TeamCalendarTicketDto> teamCalendarTicketDtoList = new ArrayList<>();

        for (MemberTeam memberTeam : memberTeams) {
            colorMap.put(memberTeam.getTeam().getId(), memberTeam.getColor());

            Member member = memberTeam.getMember();
            List<Ticket> memberTickets = member.getTicketList();

            for (Ticket ticket : memberTickets) {
                Color ticketColor = colorMap.get(ticket.getTeam().getId());
                String ticketHex = (ticketColor != null) ? ticketColor.getHex() : null;

                TeamResponseDto.TeamCalendarTicketDto ticketDto = TeamResponseDto.TeamCalendarTicketDto.builder()
                        .ticketId(ticket.getId())
                        .ticketHex(ticketHex)
                        .startTime(ticket.getStartTime())
                        .endTime(ticket.getEndTime())
                        .build();

                teamCalendarTicketDtoList.add(ticketDto);
            }
        }

        List<ShowTicketDto> showTicketDtoList = ticketList.stream()
                .filter(ticket -> team.getId().equals(ticket.getTeam().getId()))
                .map(ticket -> {

                    return ShowTicketDto.builder()
                            .ticketId(ticket.getId())
                            .workerName(ticket.getWorker().getUsername())
                            .sequence(ticket.getSequence())
                            .title(ticket.getTitle())
                            .description(ticket.getDescription())
                            .status(String.valueOf(ticket.getStatus()))
                            .endTime(ticket.getEndTime())
                            .build();
                }).toList();

        return TeamResponseDto.TeamCalendarDto.builder()
                .teamCalendarTicketDtoList(teamCalendarTicketDtoList)
                .teamName(team.getName())
                .showTicketDtoList(showTicketDtoList)
                .build();
    }
}
