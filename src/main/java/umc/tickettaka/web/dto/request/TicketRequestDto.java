package umc.tickettaka.web.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import lombok.Getter;

public class TicketRequestDto {

    @Getter
    public static class CreateTicketDto {
        String title;
        String description;
        String workerName;
        List<String> reviewerNameList;
        @JsonFormat(pattern = "yyyy-MM-dd")
        String startTime;
        @JsonFormat(pattern = "yyyy-MM-dd")
        String endTime;
    }

    @Getter
    public static class DeleteTicketDto {
        Long ticketId;
    }
}
