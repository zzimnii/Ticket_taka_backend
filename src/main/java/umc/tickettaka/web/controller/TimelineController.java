package umc.tickettaka.web.controller;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import umc.tickettaka.converter.TimelineConverter;
import umc.tickettaka.domain.Timeline;
import umc.tickettaka.payload.ApiResponse;
import umc.tickettaka.service.TimelineCommandService;
import umc.tickettaka.service.TimelineQueryService;
import umc.tickettaka.web.dto.request.TimelineRequestDto;
import umc.tickettaka.web.dto.response.TimelineResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams/{teamsId}/projects/{projectId}/timelines")
@Slf4j
public class TimelineController {

    private final TimelineCommandService timelineCommandService;
    private final TimelineQueryService timelineQueryService;

    @PostMapping(value = "/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<TimelineResponseDto.CreateResultDto> createTimeline(
        @PathVariable Long projectId,
        @RequestPart(value = "image", required = false) MultipartFile image,
        @RequestPart(value = "request") TimelineRequestDto.CreateTimelineDto request
        ) throws IOException {

        Timeline timeline = timelineCommandService.createTimeline(projectId, image, request);
        return ApiResponse.onCreate(TimelineConverter.toCreateResultDto(timeline));
    }

    @GetMapping("")
    public ApiResponse<TimelineResponseDto.ShowTimelineListDto> allTimelines(@PathVariable Long projectId) {
        List<Timeline> timelineList = timelineQueryService.findAllByProjectId(projectId);

        return ApiResponse.onSuccess(TimelineConverter.toShowTimelineListDto(timelineList));
    }
}
