package umc.tickettaka.service;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import umc.tickettaka.domain.ticket.Ticket;
import umc.tickettaka.web.dto.request.TicketRequestDto;

public interface TicketCommandService {

    Ticket createTicket(Long timelineId, List<MultipartFile> files, TicketRequestDto.CreateTicketDto request) throws IOException;
}
