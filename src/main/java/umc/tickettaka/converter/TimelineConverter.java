package umc.tickettaka.converter;

import java.util.List;
import umc.tickettaka.domain.Timeline;
import umc.tickettaka.web.dto.response.TimelineResponseDto;
import umc.tickettaka.web.dto.response.TimelineResponseDto.CreateResultDto;
import umc.tickettaka.web.dto.response.TimelineResponseDto.ShowTimelineDto;
import umc.tickettaka.web.dto.response.TimelineResponseDto.ShowTimelineListDto;

public class TimelineConverter {

    public static TimelineResponseDto.CreateResultDto toCreateResultDto(Timeline timeline) {
        return CreateResultDto.builder()
            .timelineId(timeline.getId())
            .createdTime(timeline.getCreatedTime())
            .build();
    }

    public static TimelineResponseDto.ShowTimelineListDto toShowTimelineListDto(List<Timeline> timelineList) {
        List<ShowTimelineDto> showTimelineDtoList = timelineList.stream()
            .map(timeline -> ShowTimelineDto.builder()
                .timelineId(timeline.getId())
                .name(timeline.getName())
                .imageUrl(timeline.getImageUrl())
                .build()).toList();

        return ShowTimelineListDto.builder()
            .projectName(getProjectName(timelineList))
            .showTimelineDtoList(showTimelineDtoList)
            .build();
    }

    private static String getProjectName(List<Timeline> timelineList) {
        if (!timelineList.isEmpty()) {
            return timelineList.get(0).getProject().getName();
        }

        return null;
    }
}
