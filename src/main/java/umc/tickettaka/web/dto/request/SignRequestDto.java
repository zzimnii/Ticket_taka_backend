package umc.tickettaka.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

@Data
@Schema(description = "jwt request dto")
public class SignRequestDto {

    @Getter
    @Schema(name = "sign in dto")
    public static class SignInDto {

        private String username;

        private String password;

    }

    @Getter
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
