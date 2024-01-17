package umc.tickettaka.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.tickettaka.domain.Project;
import umc.tickettaka.repository.ProjectRepository;
import umc.tickettaka.service.ProjectQueryService;

@Service
@RequiredArgsConstructor
public class ProjectQueryServiceImpl implements ProjectQueryService {

    private final ProjectRepository projectRepository;

    @Override
    public List<Project> findAllByTeamId(Long teamId) {
        return projectRepository.findAllByTeamId(teamId);
    }
}
