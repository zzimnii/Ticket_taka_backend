package umc.tickettaka.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import umc.tickettaka.domain.Project;
import umc.tickettaka.web.dto.request.ProjectRequestDto;

public interface ProjectCommandService{

    Project createProject(Long teamId, MultipartFile image, ProjectRequestDto.CreateProjectDto request) throws IOException;
}
