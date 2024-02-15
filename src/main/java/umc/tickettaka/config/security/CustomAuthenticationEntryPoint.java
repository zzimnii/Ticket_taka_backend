package umc.tickettaka.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import umc.tickettaka.payload.ApiResponse;
import umc.tickettaka.payload.status.ErrorStatus;

import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json; charset=UTF-8");
            ApiResponse<Object> apiResponse = ApiResponse.onFailure(ErrorStatus.TOKEN_NOT_FOUND.getCode(), ErrorStatus.TOKEN_NOT_FOUND.getMessage(), null);
            ObjectMapper objectMapper = new ObjectMapper();
            String writeValueAsString = objectMapper.writeValueAsString(apiResponse);
            response.getWriter().write(writeValueAsString);
        }
    }
}
