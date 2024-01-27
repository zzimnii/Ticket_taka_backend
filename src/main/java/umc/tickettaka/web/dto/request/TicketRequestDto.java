package umc.tickettaka.web.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateFeedbackDto {
        Long ticketId;
        String reviewer;
        List<String> linkList;
    }

    @Getter
    public static class DeleteTicketDto {
        Long ticketId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RejectFeedbackDto {
        Long feedbackId;
        String rejectComment;
    }
}
