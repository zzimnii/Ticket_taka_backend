package umc.tickettaka.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class TeamResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamDto{
        Long teamId;
        String teamName;
        String imageUrl;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamAndInvitationListDto {
        List<TeamDto> teamDtoList;
        List<InvitationResponseDto.InvitationDto> invitationDtoList;
    }
}
