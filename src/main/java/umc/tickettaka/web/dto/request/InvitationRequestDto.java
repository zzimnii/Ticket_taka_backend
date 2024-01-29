package umc.tickettaka.web.dto.request;

import lombok.Getter;

import java.util.List;

public class InvitationRequestDto {

    @Getter
    public static class CreateInvitationDto{
        List<String> invitedUsernameList;
    }
    @Getter
    public static class AcceptInvitationDto{
        Long invitationId;
        Boolean accept;
    }
}
