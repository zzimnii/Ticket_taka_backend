package umc.tickettaka.web.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class CommonTicketDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShowTicketDto {

        Long ticketId;
        String ticketHex;
        Long sequence;
        String title;
        String description;
        List<String> fileUrlList;
        String status;
        @JsonFormat(pattern = "yyyy-MM-dd", locale = "Asia/Seoul")
        LocalDate startTime;
        @JsonFormat(pattern = "yyyy-MM-dd", locale = "Asia/Seoul")
        LocalDate endTime;
        boolean isMyTicket;
    }
}
