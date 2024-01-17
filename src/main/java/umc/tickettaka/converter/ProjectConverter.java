package umc.tickettaka.converter;

import umc.tickettaka.domain.Project;
import umc.tickettaka.web.dto.response.ProjectResponseDto;
import umc.tickettaka.web.dto.response.ProjectResponseDto.CreateResultDto;

public class ProjectConverter {

    public static ProjectResponseDto.CreateResultDto toCreateResultDto(Project project) {
        return CreateResultDto.builder()
            .projectId(project.getId())
            .createdTime(project.getCreatedTime())
            .build();
    }
}
