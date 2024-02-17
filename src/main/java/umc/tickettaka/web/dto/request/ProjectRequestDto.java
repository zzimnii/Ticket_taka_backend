package umc.tickettaka.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

public class ProjectRequestDto {

    @Getter
    public static class CreateProjectDto {
        @NotBlank(message = "project name이 입력되지 않았습니다.")
        String name;

        @NotBlank (message = "project description이 입력되지 않았습니다.")
        @Size(max = 500, message = "길이가 500 이상입니다.")
        String description;
    }

    @Getter
    public static class UpdateProjectDto {
        @NotBlank(message = "project name이 입력되지 않았습니다.")
        String name;

        @NotBlank(message = "project description이 입력되지 않았습니다.")
        @Size(max = 500, message = "길이가 500 이상입니다.")
        String desciption;
    }

    @Getter
    public static class DeleteProjectDto {
        @NotNull(message = "project id가 입력되지 않았습니다.")
        List<Long> projectIdList;
    }
}
