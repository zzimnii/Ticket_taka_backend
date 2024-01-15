package umc.tickettaka.web.service;

import umc.tickettaka.domain.Team;
import umc.tickettaka.web.dto.teamDto.RequestDto;

public interface TeamCommandService {
    Team createTeam(RequestDto.TeamRequestDto request);
}