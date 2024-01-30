package umc.tickettaka.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class TimelineRequestDto {

    @Getter
    public static class CreateTimelineDto {
        @NotBlank(message = "timeline name이 입력되지 않았습니다.")
        String name;
    }

    @Getter
    public static class DeleteTimelineDto {
        @NotNull
        Long timelineId;
    }
}
