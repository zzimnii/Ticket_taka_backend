package umc.tickettaka.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;
import umc.tickettaka.domain.Project;
import umc.tickettaka.web.dto.request.ProjectRequestDto;
import umc.tickettaka.web.dto.response.ProjectResponseDto;

public interface ProjectCommandService{

    Project createProject(Long teamId, MultipartFile image, ProjectRequestDto.CreateProjectDto request) throws IOException;
    ProjectResponseDto.ProjectMainDto getProjectMainDto(Long teamId, Long projectId);
    Project updateProject(Long teamId, Long projectId, MultipartFile image, ProjectRequestDto.CreateProjectDto update) throws IOException;
    void deleteProject(Long teamId, Long projectId);
}
