package umc.tickettaka.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Schema(description = "member response dto")
public class MemberResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShowMemberDto {
        private Long memberId;

        private String name;

        private String imageUrl;

        private String username;

        private String email;

    }
}
