package umc.tickettaka.web.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.tickettaka.web.dto.response.ProjectResponseDto;

import java.time.LocalDateTime;
import java.util.List;


public class CommonMemberDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShowMemberProfileListDto {
        List<CommonMemberDto.ShowMemberProfileDto> showMemberProfileDtoList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShowMemberProfileDto {
        String imageUrl;
        String name;
        String memberHex;
    }
}
