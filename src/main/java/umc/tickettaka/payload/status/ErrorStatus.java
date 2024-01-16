package umc.tickettaka.payload.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import umc.tickettaka.payload.BaseErrorCode;
import umc.tickettaka.payload.dto.ErrorReasonDto;
import umc.tickettaka.payload.exception.GeneralException;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON4000", "Bad request"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "COMMON4001",  "Validation error"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON4002", "Requested resource not found"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON5000",  "Internal error"),
    DATA_ACCESS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON5001",  "Data access error"),

    //MEMBER Error
    MEMBER_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "MEMBER4000", "Member unauthorized"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER4001", "Member Not found"),
    MEMBER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4002", "Member already exists");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public String getMessage(Throwable e) {
        return this.getMessage(this.getMessage() + " - " + e.getMessage());
        // 결과 예시 - "Validation error - Reason why it isn't valid"
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }

    @Override
    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder()
            .message(message)
            .code(code)
            .isSuccess(false)
            .build();
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
            .message(message)
            .code(code)
            .isSuccess(false)
            .httpStatus(httpStatus)
            .build();
    }
}
