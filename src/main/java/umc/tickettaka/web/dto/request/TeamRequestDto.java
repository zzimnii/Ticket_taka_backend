package umc.tickettaka.web.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class TeamRequestDto {

    @Getter
    public static class CreateTeamDto{
        @NotBlank(message = "team의 name이 입력되지 않았습니다.")
        String teamName;

        List<@NotBlank(message = "초대할 id(username)이 공백입니다.") String> invitedUsernameList;
    }
}
