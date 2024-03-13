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
@RedisHash(value = "blackList", timeToLive = 60 * 30)
@Builder
public class BlackList {

    @Id @Indexed
    private String accessToken;

    private Boolean isLogOut;
}
