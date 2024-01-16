package umc.tickettaka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.tickettaka.converter.TeamConverter;
import umc.tickettaka.domain.Team;
import umc.tickettaka.web.dto.request.TeamRequestDto;
import umc.tickettaka.repository.TeamRepository;

@Service
@RequiredArgsConstructor
public class TeamCommandServiceImpl implements TeamCommandService{

    private final TeamRepository teamRepository;

    @Override
    public Team createTeam(TeamRequestDto.TeamDto request) {
        Team newTeam = TeamConverter.toTeam(request);

        return teamRepository.save(newTeam);
    }
}
