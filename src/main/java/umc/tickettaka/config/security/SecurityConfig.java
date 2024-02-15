package umc.tickettaka.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import umc.tickettaka.config.security.jwt.ExceptionHandlerFilter;
import umc.tickettaka.config.security.jwt.JwtAuthenticationFilter;
import umc.tickettaka.config.security.jwt.JwtTokenProvider;
import umc.tickettaka.oauth.CustomOAuth2SuccessHandler;
import umc.tickettaka.oauth.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authHttp -> authHttp.requestMatchers("/*").permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/health")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/members/sign-up")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/members/sign-in")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/auth/**")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/swagger-resources/**")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
                                .anyRequest().authenticated()

                )
                .exceptionHandling(basic -> basic.authenticationEntryPoint(customAuthenticationEntryPoint))
                .oauth2Login(
                        oauth2Login -> oauth2Login
                        .loginPage("/login")
                        .successHandler(customOAuth2SuccessHandler)
                        .userInfoEndpoint(userInfo ->
                                userInfo.userService(customOAuth2UserService))
                )
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new ExceptionHandlerFilter(), JwtAuthenticationFilter.class) // exception이 발생하면 그 앞 filter에서 처리하므로
        ;


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
