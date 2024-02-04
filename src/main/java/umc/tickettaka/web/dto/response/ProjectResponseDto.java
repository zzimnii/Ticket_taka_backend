package umc.tickettaka.web.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.tickettaka.web.dto.common.CommonMemberDto;

public class ProjectResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateResultDto {
        Long projectId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShowProjectListDto {
        CommonMemberDto.ShowMemberProfileListDto memberProfileList;
        List<ShowProjectDto> showProjectDtoList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShowProjectDto {
        Long projectId;
        String name;
        String imageUrl;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectMainDto {
        String teamName;
        String projectName;
        List<TicketResponseDto.MemberAchieveLevelDto> memberListDto;
        String projectDescription;
        List<String> linkList;
    }
}
