package umc.tickettaka.web.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.tickettaka.web.dto.common.CommonMemberDto;
import umc.tickettaka.web.dto.common.CommonTicketDto.ShowTicketDto;

public class TicketResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateTicketResultDto {
        Long id;
        Long sequence;
        LocalDateTime createdTime;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShowAllTicketListDto {

        String timelineName;
        List<ShowTicketDto> ticketDtoList;

        CommonMemberDto.ShowMemberProfileListDto memberProfileListDto;
    }
}
