package umc.tickettaka.web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.tickettaka.converter.TeamConverter;
import umc.tickettaka.domain.Team;
import umc.tickettaka.web.dto.teamDto.RequestDto;
import umc.tickettaka.web.repository.TeamRepository;

@Service
@RequiredArgsConstructor
public class TeamCommandServiceImpl implements TeamCommandService{

    private final TeamRepository teamRepository;

    @Override
    public Team createTeam(RequestDto.TeamRequestDto request) {
        Team newTeam = TeamConverter.toCreateTeam(request);

        return teamRepository.save(newTeam);
    }
}
