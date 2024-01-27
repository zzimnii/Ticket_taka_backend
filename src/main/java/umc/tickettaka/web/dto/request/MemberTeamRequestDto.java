package umc.tickettaka.web.dto.request;

import lombok.Getter;
import umc.tickettaka.domain.enums.Color;

public class MemberTeamRequestDto {

    @Getter
    public static class UpdateColorDto {
        Color color;
    }
}
