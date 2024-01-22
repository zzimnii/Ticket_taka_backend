package umc.tickettaka.web.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TicketResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateTicketResultDto {
        Long id;
        Long sequence;
        LocalDateTime createdTime;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShowTicketDto {
        Long ticketId;
        Long sequence;
        String title;
        String description;
        List<String> fileUrlList;
        String status;
        LocalDate endTime;
    }
}
