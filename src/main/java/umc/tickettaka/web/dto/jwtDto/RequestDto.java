package umc.tickettaka.web.dto.jwtDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "jwt request dto")
public class RequestDto {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "jwt request dto")
    public static class SignInDto {

        private String username;

        private String password;

    }
}
