package umc.tickettaka.web.dto.common;

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
                String workerName;
                Long sequence;
                String title;
                String description;
                List<String> fileUrlList;
                String status;
                LocalDate startTime;
                LocalDate endTime;
                boolean isMyTicket;
        }
}
