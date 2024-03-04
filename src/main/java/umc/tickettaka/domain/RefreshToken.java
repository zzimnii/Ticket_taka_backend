package umc.tickettaka.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@AllArgsConstructor
@Slf4j
@Getter
@RedisHash(value = "jwtToken", timeToLive = 60 * 60 * 24 * 7)
@Builder
public class RefreshToken {

    @Id @Indexed
    private String accessToken;

    private String refreshToken;



}
