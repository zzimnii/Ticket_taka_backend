package umc.tickettaka.converter;

import umc.tickettaka.domain.enums.FeedbackStatus;
import umc.tickettaka.domain.ticket.Feedback;
import umc.tickettaka.domain.ticket.Ticket;

import java.util.List;

public class FeedbackConverter {
    public static Feedback toFeedback(Ticket ticket, List<String> fileLinks, List<String> linkList) {
        return Feedback.builder()
                .ticket(ticket)
                .fileList(fileLinks)
                .linkList(linkList)
                .build();
    }
}
