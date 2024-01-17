package umc.tickettaka.web.dto.request;

import lombok.Getter;

public class ProjectRequestDto {

    @Getter
    public static class CreateProjectDto {
        String name;
        String description;
    }


}
