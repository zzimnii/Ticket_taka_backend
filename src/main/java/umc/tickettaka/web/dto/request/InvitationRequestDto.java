package umc.tickettaka.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

public class InvitationRequestDto {

    @Getter
    public static class CreateInvitationDto{
        List<String> invitedUsernameList;
    }
    @Getter
    public static class AcceptInvitationDto{
        @NotNull
        Long invitationId;

        Boolean accept;
    }
}
