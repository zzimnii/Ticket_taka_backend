package umc.tickettaka.converter;

import java.util.List;
import umc.tickettaka.domain.Project;
import umc.tickettaka.web.dto.response.ProjectResponseDto;
import umc.tickettaka.web.dto.response.ProjectResponseDto.CreateResultDto;
import umc.tickettaka.web.dto.response.ProjectResponseDto.ShowProjectDto;
import umc.tickettaka.web.dto.response.ProjectResponseDto.ShowProjectListDto;
import umc.tickettaka.web.dto.response.TicketResponseDto;

public class ProjectConverter {

    public static ProjectResponseDto.CreateResultDto toCreateResultDto(Project project) {
        return CreateResultDto.builder()
            .projectId(project.getId())
            .createdTime(project.getCreatedTime())
            .build();
    }

    public static ProjectResponseDto.ShowProjectDto toShowProjectDto(Project project) {
        return ShowProjectDto.builder()
                .projectId(project.getId())
                .name(project.getName())
                .imageUrl(project.getImageUrl())
                .modifiedTime(project.getUpdatedTime())
                .build();
    }

    public static ProjectResponseDto.ShowProjectListDto toShowProjectListDto(List<Project> projectList) {
        List<ShowProjectDto> showProjectDtoList = projectList.stream()
            .map(project -> ShowProjectDto.builder()
                .projectId(project.getId())
                .name(project.getName())
                .imageUrl(project.getImageUrl())
                .modifiedTime(project.getUpdatedTime())
                .build()).toList();

        return ShowProjectListDto.builder()
            .showProjectDtoList(showProjectDtoList)
            .build();
    }

    public static ProjectResponseDto.ProjectMainDto toShowProjectMainDto (
            String teamName,
            String projectName,
            List<TicketResponseDto.MemberAchieveLevelDto> memberAchieveLevelDtoList,
            String projectDescription,
            List<String> linkUrlList) {

        return ProjectResponseDto.ProjectMainDto.builder()
                .teamName(teamName)
                .projectName(projectName)
                .memberListDto(memberAchieveLevelDtoList)
                .projectDescription(projectDescription)
                .linkUrls(linkUrlList)
                .build();
    }
}
