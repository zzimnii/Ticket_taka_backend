package umc.tickettaka.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "jwt request dto")
public class SignRequestDto {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "sign in dto")
    public static class SignInDto {

        private String username;

        private String password;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "sign up dto")
    public static class SignUpDto {

        private String username;

        private String password;

        private String email;

        private String imageUrl;

        private String providerType;

        private String providerId;

    }
}
