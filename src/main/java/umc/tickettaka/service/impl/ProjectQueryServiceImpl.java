package umc.tickettaka.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.tickettaka.domain.Project;
import umc.tickettaka.payload.exception.GeneralException;
import umc.tickettaka.payload.status.ErrorStatus;
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

    @Override
    public Project findById(Long id) {
        return projectRepository.findById(id)
            .orElseThrow(() -> new GeneralException(ErrorStatus.PROJECT_NOT_FOUND));
    }
}
