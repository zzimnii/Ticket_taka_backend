package umc.tickettaka.web.dto.response;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommonTicketDto {
        Long ticketId;
        Long sequence;
        String title;
        String description;
        List<String> fileUrlList;
        String status;
        LocalDate endTime;
}
