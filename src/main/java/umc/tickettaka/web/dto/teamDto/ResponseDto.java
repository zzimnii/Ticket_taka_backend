package umc.tickettaka.web.dto.teamDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamResponseDto{
        Long teamId;
        String name;
        LocalDateTime createdAt;
    }
}
