package umc.tickettaka.web.dto.teamDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class RequestDto {
    @Getter
    public static class TeamRequestDto{
        String name;
        String imageUrl;
    }
}
