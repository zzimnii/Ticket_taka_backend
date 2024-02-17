package umc.tickettaka.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import umc.tickettaka.web.dto.common.CommonMemberDto.ShowMemberProfileDto;
import umc.tickettaka.web.dto.common.CommonTicketDto.ShowTicketDto;
import umc.tickettaka.web.dto.response.TeamResponseDto.TeamSelectDto;

@Data
@Schema(description = "member response dto")
public class MemberResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShowMemberDto {
        private Long memberId;

        private String name;

        private String imageUrl;

        private String username;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPageDto {
        ShowMemberProfileDto profileDto;
        List<ShowTicketDto> showTicketDtoList;
        List<TeamSelectDto> teamSelectDtoList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPageTicketDto {
        Long ticketId;
        String ticketHex;
        Long ticketSequence;
        String title;
        String description;
        String status;
        @JsonFormat(pattern = "yyyy-MM-dd", locale = "Asia/Seoul")
        LocalDate endTime;
        List<String> fileUrlList;
        Long teamId;
        String teamName;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenDto {
        String accessToken;
    }
}
