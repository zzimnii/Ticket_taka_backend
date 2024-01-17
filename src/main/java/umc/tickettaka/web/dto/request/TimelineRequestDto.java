package umc.tickettaka.web.dto.request;

import lombok.Getter;

public class TimelineRequestDto {

    @Getter
    public static class CreateTimelineDto {
        String name;
    }
}
