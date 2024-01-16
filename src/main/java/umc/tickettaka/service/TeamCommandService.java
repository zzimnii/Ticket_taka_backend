package umc.tickettaka.service;

import umc.tickettaka.domain.Team;
import umc.tickettaka.web.dto.request.TeamRequestDto;

public interface TeamCommandService {
    Team createTeam(TeamRequestDto.TeamDto request);
}