package umc.tickettaka.service;

import umc.tickettaka.domain.Team;
import java.util.Optional;

public interface TeamQueryService {
    Optional<Team> findTeam(Long id);
}