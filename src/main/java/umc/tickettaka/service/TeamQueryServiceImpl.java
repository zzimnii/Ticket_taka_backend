package umc.tickettaka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.tickettaka.domain.Team;
import umc.tickettaka.repository.TeamRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamQueryServiceImpl implements TeamQueryService{

    private final TeamRepository teamRepository;

    @Override
    public Optional<Team> findTeam(Long id) {
        return teamRepository.findById(id);
    }
}