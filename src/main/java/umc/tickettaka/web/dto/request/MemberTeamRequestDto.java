package umc.tickettaka.web.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import umc.tickettaka.domain.enums.Color;

public class MemberTeamRequestDto {

    @Getter
    public static class UpdateColorDto {
        @NotNull(message = "변경할 color가 선택되지 않았습니다.")
        Color color;
    }
}
