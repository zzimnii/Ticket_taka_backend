package umc.tickettaka.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.tickettaka.web.dto.common.CommonMemberDto;
import umc.tickettaka.web.dto.common.CommonTicketDto.ShowTicketDto;

import java.time.LocalDate;
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

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamCalendarTicketDto {
        Long ticketId;
        String ticketHex;
        @JsonFormat(pattern = "yyyy-MM-dd", locale = "Asia/Seoul")
        LocalDate startTime;
        @JsonFormat(pattern = "yyyy-MM-dd", locale = "Asia/Seoul")
        LocalDate endTime;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamCalendarDto {
        String teamName;
        List<TeamCalendarTicketDto> teamCalendarTicketDtoList;
        List<ShowTicketDto> showTicketDtoList;
        CommonMemberDto.ShowMemberProfileListDto memberProfileListDto;
    }
}
