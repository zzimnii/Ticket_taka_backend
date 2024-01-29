package umc.tickettaka.service;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.ticket.Ticket;
import umc.tickettaka.web.dto.common.CommonMemberDto;
import umc.tickettaka.web.dto.request.TicketRequestDto;
import umc.tickettaka.web.dto.request.TicketRequestDto.DeleteTicketDto;
import umc.tickettaka.web.dto.response.TicketResponseDto;

public interface TicketCommandService {

    Ticket createTicket(Long timelineId, List<MultipartFile> files, TicketRequestDto.CreateTicketDto request) throws IOException;

    TicketResponseDto.MemberAchieveLevelDto showMemberAchieve(Member member, Long teamId);

    void makeFeedbackRequest(TicketRequestDto.CreateFeedbackDto feedbackDto, List<MultipartFile> files)throws IOException;

    void acceptFeedback(Long ticketId);

    public void rejectFeedback(TicketRequestDto.RejectFeedbackDto reject);
    void deleteTicket(DeleteTicketDto request);
}
