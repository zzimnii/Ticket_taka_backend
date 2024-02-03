package umc.tickettaka.web.controller;

import java.io.IOException;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.tickettaka.converter.ProjectConverter;
import umc.tickettaka.domain.Project;
import umc.tickettaka.payload.ApiResponse;
import umc.tickettaka.service.MemberCommandService;
import umc.tickettaka.service.ProjectCommandService;
import umc.tickettaka.service.ProjectQueryService;
import umc.tickettaka.web.dto.common.CommonMemberDto;
import umc.tickettaka.web.dto.request.ProjectRequestDto;
import umc.tickettaka.web.dto.response.ProjectResponseDto;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/teams/{teamId}/projects")
public class ProjectController {

    private final MemberCommandService memberCommandService;
    private final ProjectCommandService projectCommandService;
    private final ProjectQueryService projectQueryService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE , MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "project 생성 요청")
    public ApiResponse<ProjectResponseDto.CreateResultDto> createProject(
        @PathVariable(name="teamId") Long teamId,
        @RequestPart(value = "image", required = false) MultipartFile image,
        @RequestPart(value = "request") @Valid ProjectRequestDto.CreateProjectDto request) throws IOException {

        Project project = projectCommandService.createProject(teamId, image, request);
        return ApiResponse.onCreate(ProjectConverter.toCreateResultDto(project));
    }

    @GetMapping("")
    @Operation(summary = "팀 공간 창 (참여 팀원, 모든 project 표시)")
    public ApiResponse<ProjectResponseDto.ShowProjectListDto> allProjects(
            @PathVariable(name = "teamId") Long teamId) {
        CommonMemberDto.ShowMemberProfileListDto memberProfileList = memberCommandService.getCommonMemberDto(teamId);
        List<Project> projectList = projectQueryService.findAllByTeamId(teamId);

        return ApiResponse.onSuccess(ProjectConverter.toShowProjectListDto(memberProfileList, projectList));
    }

    @GetMapping("/{projectId}")
    @Operation(summary = "각 project 상세창")
    public ApiResponse<ProjectResponseDto.ProjectMainDto> project(
            @PathVariable(name = "teamId") Long teamId,
            @PathVariable(name = "projectId") Long projectId) {

        return ApiResponse.onSuccess(projectCommandService.getProjectMainDto(teamId,projectId));
    }

    @PatchMapping(value="/{projectId}", consumes = {MediaType.APPLICATION_JSON_VALUE , MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "project 정보 수정 요청")
    public ApiResponse<ProjectResponseDto.ShowProjectDto> updateProject(
            @PathVariable(name = "teamId") Long teamId,
            @PathVariable(name = "projectId") Long projectId,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "request", required = false) @Valid ProjectRequestDto.UpdateProjectDto request) throws IOException{
        Project project = projectCommandService.updateProject(teamId,projectId,image,request);

        return ApiResponse.onSuccess(ProjectConverter.toShowProjectDto(project));
    }

    @DeleteMapping
    @Operation(summary = "project 삭제 요청")
    public ApiResponse<ProjectResponseDto.ShowProjectListDto> deleteProject(
            @PathVariable Long teamId,
            @RequestParam Long projectId) {
        projectCommandService.deleteProject(teamId, projectId);
        return ApiResponse.onSuccess(null);
    }

}
