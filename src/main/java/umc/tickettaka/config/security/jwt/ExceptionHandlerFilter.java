package umc.tickettaka.config.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import umc.tickettaka.payload.ApiResponse;
import umc.tickettaka.payload.status.ErrorStatus;

import java.io.IOException;
import java.nio.charset.MalformedInputException;

@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, e, ErrorStatus.EXPIRED_ACCESS_TOKEN);
        } catch (UnsupportedJwtException e) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, e, ErrorStatus.UNSUPPORTED_TOKEN);
        } catch (Exception e) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, e, ErrorStatus.INVALID_TOKEN);
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, Throwable ex, ErrorStatus errorStatus) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json; charset=UTF-8");

        ApiResponse<Object> apiResponse = ApiResponse.onFailure(errorStatus.getCode(), errorStatus.getMessage(), null);
        ObjectMapper objectMapper = new ObjectMapper();
        String writeValueAsString = objectMapper.writeValueAsString(apiResponse);
        response.getWriter().write(writeValueAsString);
    }
}
