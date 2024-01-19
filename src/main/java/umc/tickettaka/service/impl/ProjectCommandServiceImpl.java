package umc.tickettaka.service.impl;

import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import umc.tickettaka.domain.Project;
import umc.tickettaka.domain.Team;
import umc.tickettaka.repository.ProjectRepository;
import umc.tickettaka.service.ImageUploadService;
import umc.tickettaka.service.ProjectCommandService;
import umc.tickettaka.service.ProjectQueryService;
import umc.tickettaka.service.TeamQueryService;
import umc.tickettaka.web.dto.request.ProjectRequestDto;

@Service
@RequiredArgsConstructor
public class ProjectCommandServiceImpl implements ProjectCommandService {

    private final TeamQueryService teamQueryService;
    private final ImageUploadService imageUploadService;
    private final ProjectRepository projectRepository;
    private final ProjectQueryService projectQueryService;

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

    @Override
    @Transactional
    public Project updateProject(Long teamId, Long projectId, MultipartFile image, ProjectRequestDto.CreateProjectDto update) throws IOException {
        Team team = teamQueryService.findTeam(teamId);
        Project project = projectQueryService.findById(projectId);
        String imageUrl = project.getImageUrl();
        String updatedName = project.getName();
        String updatedDescription = project.getDescription();

        if (image != null) {
            imageUrl = imageUploadService.uploadImage(image);
        }
        if (update != null) {
            updatedName = update.getName();
            updatedDescription = update.getDescription();
        }

        project = Project.builder()
                .id(projectId)
                .name(updatedName)
                .description(updatedDescription)
                .imageUrl(imageUrl)
                .team(team)
                .build();

        return projectRepository.save(project);
    }

    /**
     * Optional<Project> optionalProject = projectRepository.findById(projectId);
     *
     *         if (optionalProject.isPresent()) {
     *             Project project = optionalProject.get();
     *
     *             String updatedName = update.getName();
     *             String updatedDescription = update.getDescription();
     *
     *             if (updatedName != null) {
     *                 project.changeName(updatedName);
     *             }
     *             if (updatedDescription != null) {
     *                 project.changeDescription(updatedDescription);
     *             }
     *
     *             return projectRepository.save(project);
     *         } else {
     *             throw new IllegalArgumentException("Project not found");
     *         }
     */
    @Override
    @Transactional
    public void deleteProject(Long teamId, Long projectId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);

        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            projectRepository.delete(project);
        } else {
            throw new IllegalArgumentException("Project not found");
        }
    }
}
