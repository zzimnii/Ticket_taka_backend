package umc.tickettaka.converter;

import umc.tickettaka.domain.Team;
import umc.tickettaka.web.dto.teamDto.RequestDto;
import umc.tickettaka.web.dto.teamDto.ResponseDto;

import java.time.LocalDateTime;

public class TeamConverter {

    public static ResponseDto.TeamResponseDto toResultDto(Team team) {
        return ResponseDto.TeamResponseDto.builder()
                .teamId(team.getId())
                .name(team.getName())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Team toCreateTeam(RequestDto.TeamRequestDto request) {
        return Team.builder()
                .name(request.getName())
                .imageUrl(request.getImageUrl())
                .build();
    }
}