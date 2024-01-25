package umc.tickettaka.service;

import java.util.List;
import umc.tickettaka.domain.Member;
import umc.tickettaka.domain.ticket.Ticket;
import umc.tickettaka.web.dto.common.CommonTicketDto.ShowTicketDto;
import umc.tickettaka.web.dto.response.TicketResponseDto;

public interface TicketQueryService {

    List<Ticket> findAllByTimelineId(Long timelineId);
    Ticket findById(Long id);
    List<Ticket> findAllByWorker(Member member);

    List<ShowTicketDto> getShowTicketDto(Member member);
    List<ShowTicketDto> getShowTicketDto(Member member, Long timelineId);

    TicketResponseDto.ShowAllTicketListDto getShowAllTicketListDto(Member member, Long teamId, Long timelineId);
}
