package umc.tickettaka.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TimelineResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateResultDto {
        Long timelineId;
        @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", locale = "Asia/Seoul")
        LocalDateTime createdTime;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShowTimelineListDto {
        String projectName;
        List<TimelineResponseDto.ShowTimelineDto> showTimelineDtoList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShowTimelineDto {
        Long timelineId;
        String name;
        String imageUrl;
    }
}
