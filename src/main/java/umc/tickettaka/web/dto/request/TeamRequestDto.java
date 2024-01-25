package umc.tickettaka.web.dto.request;

import java.util.List;
import lombok.Getter;

public class TeamRequestDto {

    @Getter
    public static class CreateTeamDto{
        String name;
        List<String> invitedUsernameList;
    }
}
