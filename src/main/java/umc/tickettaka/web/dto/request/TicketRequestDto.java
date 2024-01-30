package umc.tickettaka.web.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TicketRequestDto {

    @Getter
    public static class CreateTicketDto {
        List<String> fileUrlList;

        @NotBlank(message = "ticket title이 입력되지 않았습니다.")
        String title;

        @NotBlank(message = "ticket description이 입력되지 않았습니다.")
        String description;

        @NotBlank(message = "ticket에 할당된 member가 없습니다.")
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
        @NotNull
        Long ticketId;

        List<@NotBlank(message = "할당된 reviewer가 없습니다.") String> reviewerList;

        List<String> linkList;
    }

    @Getter
    public static class DeleteTicketDto {
        @NotNull
        Long ticketId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RejectFeedbackDto {
        @NotNull
        Long feedbackId;

        @NotBlank(message = "feedback을 거절할 때는 comment가 필요합니다.")
        String rejectComment;
    }
}
