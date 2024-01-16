package umc.tickettaka.web.dto.request;

import lombok.Getter;

public class TeamRequestDto {

    @Getter
    public static class TeamDto{
        String name;
        String imageUrl;
    }
}
