package umc.tickettaka.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.tickettaka.web.dto.response.TeamResponseDto.TeamDto;

import java.util.List;

public class InvitationResponseDto {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvitationDto {
        Long id;
        TeamDto teamDto;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvitationListDto {
        List<InvitationDto> invitationDtoList;
    }
}
