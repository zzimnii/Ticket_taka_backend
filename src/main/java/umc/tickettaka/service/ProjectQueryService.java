package umc.tickettaka.service;

import java.util.List;
import umc.tickettaka.domain.Project;

public interface ProjectQueryService {

    List<Project> findAllByTeamId(Long teamId);
}
