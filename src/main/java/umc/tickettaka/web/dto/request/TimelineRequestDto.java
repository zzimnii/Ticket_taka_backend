package umc.tickettaka.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class TimelineRequestDto {

    @Getter
    public static class CreateTimelineDto {
        @NotBlank(message = "timeline name이 입력되지 않았습니다.")
        String name;
    }

    @Getter
    public static class DeleteTimelineListDto {
        @NotNull(message = "timeline list가 입력 되지 않았습니다.")
        List<Long> timelineIdList;
    }
}
