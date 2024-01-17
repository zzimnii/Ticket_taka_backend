package umc.tickettaka.web.controller;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import umc.tickettaka.converter.ProjectConverter;
import umc.tickettaka.domain.Project;
import umc.tickettaka.payload.ApiResponse;
import umc.tickettaka.service.ProjectCommandService;
import umc.tickettaka.web.dto.request.ProjectRequestDto;
import umc.tickettaka.web.dto.response.ProjectResponseDto;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/teams/{teamId}/projects")
public class ProjectController {

    private final ProjectCommandService projectCommandService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE , MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<ProjectResponseDto.CreateResultDto> createProject(
        @PathVariable Long teamId,
        @RequestPart(value = "image", required = false) MultipartFile image,
        @RequestPart(value = "request") ProjectRequestDto.CreateProjectDto request) throws IOException {

        Project project = projectCommandService.createProject(teamId, image, request);
        return ApiResponse.onCreate(ProjectConverter.toCreateResultDto(project));
    }
}
