package umc.tickettaka.web.dto.request;

import java.util.List;
import lombok.Getter;

public class TicketRequestDto {

    @Getter
    public static class CreateTicketDto {
        String title;
        String description;
        String workerName;
        List<String> reviewerNameList;
        int startYear;
        int startMonth;
        int startDay;
        int endYear;
        int endMonth;
        int endDay;
    }

    @Getter
    public static class DeleteTicketDto {
        Long ticketId;
    }
}
