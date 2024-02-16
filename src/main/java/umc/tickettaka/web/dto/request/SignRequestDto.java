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

        @Schema(description = "NAVER, KAKAO, GOOGLE 셋 중 하나입니다. 현재 null로만 요청 가능합니다.")
        private String providerType;

        @Schema(description = "소셜 로그인에서만 사용합니다.")
        private String email;

    }
}
