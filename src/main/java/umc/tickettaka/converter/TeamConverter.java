package umc.tickettaka.converter;

import umc.tickettaka.domain.Team;
import umc.tickettaka.web.dto.request.TeamRequestDto;
import umc.tickettaka.web.dto.response.TeamResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TeamConverter {

    public static TeamResponseDto.TeamDto toTeamResultDto(Team team) {
        return TeamResponseDto.TeamDto.builder()
                .teamId(team.getId())
                .name(team.getName())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Team toTeam(TeamRequestDto.TeamDto request, String imageUrl) {
        return Team.builder()
                .name(request.getName())
                .imageUrl(imageUrl)
                .build();
    }

    public static TeamResponseDto.TeamListDto toTeamListDto(List<Team> teamList) {
        List<TeamResponseDto.TeamDto> teamDtoList = teamList.stream()
                .map(TeamConverter::toTeamResultDto)
                .collect(Collectors.toList());

        return TeamResponseDto.TeamListDto.builder()
                .teamDtoList(teamDtoList)
                .build();
    }
}
