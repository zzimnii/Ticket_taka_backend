package umc.tickettaka.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Schema(description = "jwt request dto")
public class SignRequestDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "sign in dto")
    public static class SignInDto {

        private String username;

        private String password;

        private Boolean keepStatus; // 로그인 유지

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "sign up dto")
    public static class SignUpDto {

        private String name;

        private String username;

        private String password;

        private String password2;

        private String imageUrl;

        private String deviceToken;

        private String providerType;

        private String email;

    }
}
