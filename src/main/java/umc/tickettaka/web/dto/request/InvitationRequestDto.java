package umc.tickettaka.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

public class InvitationRequestDto {

    @Getter
    public static class CreateInvitationDto{
        List<@NotBlank(message = "초대할 팀원이 입력되지 않았습니다.") String> invitedUsernameList;
    }
    @Getter
    public static class AcceptInvitationDto{
        @NotNull
        Long invitationId;

        Boolean accept;
    }
}
