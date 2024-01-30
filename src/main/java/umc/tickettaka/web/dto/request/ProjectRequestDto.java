package umc.tickettaka.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public class ProjectRequestDto {

    @Getter
    public static class CreateProjectDto {
        @NotBlank
        String name;

        @NotBlank
        @Size(max = 500)
        String description;
    }

    @Getter
    public static class UpdateProjectDto {
        @NotBlank
        String name;

        @NotBlank
        @Size(max = 500)
        String desciption;
    }


}
