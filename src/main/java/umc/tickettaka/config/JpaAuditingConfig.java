package umc.tickettaka.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// I made the EnableJpaAuditing configuration class rather than putting annotation on TicketTakaApplication.class
// this is for the @WebMvcTest test code. you guys can see the more specific reason for this Configuration
// https://velog.io/@peeeeer/JPA-EnableJpaAuditing%EC%9D%84-%EB%A9%94%EC%9D%B8-%ED%81%B4%EB%9E%98%EC%8A%A4%EC%97%90-%EC%A0%81%EC%9A%A9%ED%95%98%EB%A9%B4-%EC%95%88%EB%90%98%EB%8A%94-%EC%9D%B4%EC%9C%A0
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
