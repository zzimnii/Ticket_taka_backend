package umc.tickettaka.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import java.io.IOException;
import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.tickettaka.converter.TimelineConverter;
import umc.tickettaka.domain.Project;
import umc.tickettaka.domain.Timeline;
import umc.tickettaka.payload.ApiResponse;
import umc.tickettaka.service.ProjectQueryService;
import umc.tickettaka.service.TimelineCommandService;
import umc.tickettaka.service.TimelineQueryService;
import umc.tickettaka.web.dto.request.TimelineRequestDto;
import umc.tickettaka.web.dto.response.TimelineResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams/{teamId}/projects/{projectId}/timelines")
@Slf4j
public class TimelineController {

    private final TimelineCommandService timelineCommandService;
    private final TimelineQueryService timelineQueryService;
    private final ProjectQueryService projectQueryService;

    @GetMapping
    @Operation(summary = "모든 Timeline 조회")
    @Parameters({
        @Parameter(name = "projectId", description = "프로젝트 아이디 : Path Variable")
    })
    public ApiResponse<TimelineResponseDto.ShowTimelineListDto> allTimelines(@PathVariable Long projectId) {
        Project project = projectQueryService.findById(projectId);
        List<Timeline> timelineList = timelineQueryService.findAllByProjectId(projectId);
        return ApiResponse.onSuccess(TimelineConverter.toShowTimelineListDto(project.getName(), timelineList));
    }

    @PostMapping(value = "/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Timeline 생성")
    @Parameters({
        @Parameter(name = "projectId", description = "프로젝트 아이디 : Path Variable")
    })
    public ApiResponse<TimelineResponseDto.CreateResultDto> createTimeline(
        @PathVariable Long projectId,
        @RequestPart(value = "image", required = false) MultipartFile image,
        @RequestPart(value = "request") @Valid TimelineRequestDto.CreateTimelineDto request
        ) throws IOException {

        Timeline timeline = timelineCommandService.createTimeline(projectId, image, request);
        return ApiResponse.onCreate(TimelineConverter.toCreateResultDto(timeline));
    }

    @DeleteMapping
    @Operation(summary = "Timeline 삭제")
    @Parameters({
        @Parameter(name = "projectId", description = "프로젝트 아이디 : Path Variable")
    })
    public ApiResponse<TimelineResponseDto.ShowTimelineListDto> deleteTimelines(
            @PathVariable(name = "projectId") Long projectId,
            @RequestBody @Valid TimelineRequestDto.DeleteTimelineDto request) {
        Project project = projectQueryService.findById(projectId);
        timelineCommandService.deleteTimeline(request);
        List<Timeline> timelineList = timelineQueryService.findAllByProjectId(projectId);

        return ApiResponse.onSuccess(TimelineConverter.toShowTimelineListDto(project.getName(), timelineList));
    }
}
