package umc.tickettaka.service.impl;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import umc.tickettaka.domain.Project;
import umc.tickettaka.domain.Team;
import umc.tickettaka.repository.ProjectRepository;
import umc.tickettaka.service.ImageUploadService;
import umc.tickettaka.service.ProjectCommandService;
import umc.tickettaka.service.TeamQueryService;
import umc.tickettaka.web.dto.request.ProjectRequestDto;

@Service
@RequiredArgsConstructor
public class ProjectCommandServiceImpl implements ProjectCommandService {

    private final TeamQueryService teamQueryService;
    private final ImageUploadService imageUploadService;
    private final ProjectRepository projectRepository;

    @Override
    @Transactional
    public Project createProject(Long teamId, MultipartFile image, ProjectRequestDto.CreateProjectDto request) throws IOException {
        Team team = teamQueryService.findTeam(teamId);
        String imageUrl = imageUploadService.uploadImage(image);

        Project project = Project.builder()
            .name(request.getName())
            .description(request.getDescription())
            .imageUrl(imageUrl)
            .team(team)
            .build();

        return projectRepository.save(project);
    }
}
