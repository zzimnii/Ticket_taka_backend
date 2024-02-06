package umc.tickettaka.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

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
    public static class MyPageMemberDto {
        private String imageUrl;
        private String name;
        private String memberHex;

        List<MyPageTicketDto> myPageTicketDtoList;

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
        String teamName;
    }

}
