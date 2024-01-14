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

    OK("COMMON2000", HttpStatus.OK, "Ok"),
    BAD_REQUEST("COMMON4000", HttpStatus.BAD_REQUEST, "Bad request"),
    VALIDATION_ERROR("COMMON4001", HttpStatus.BAD_REQUEST, "Validation error"),
    NOT_FOUND("COMMON4002", HttpStatus.NOT_FOUND, "Requested resource is not found"),
    INTERNAL_ERROR("COMMON5000", HttpStatus.INTERNAL_SERVER_ERROR, "Internal error"),
    DATA_ACCESS_ERROR("COMMON5001", HttpStatus.INTERNAL_SERVER_ERROR, "Data access error"),

    //MEMBER Error
    UNAUTHORIZED("MEMBER4000", HttpStatus.UNAUTHORIZED, "Member unauthorized");

    private final String code;
    private final HttpStatus httpStatus;
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

    public static ErrorStatus valueOf(HttpStatus httpStatus) {
        if (httpStatus == null) {
            throw new GeneralException("HttpStatus is null.");
        }

        return Arrays.stream(values())
                .filter(errorCode -> errorCode.getHttpStatus() == httpStatus)
                .findFirst()
                .orElseGet(() -> {
                    if (httpStatus.is4xxClientError()) {
                        return ErrorStatus.BAD_REQUEST;
                    } else if (httpStatus.is5xxServerError()) {
                        return ErrorStatus.INTERNAL_ERROR;
                    } else {
                        return ErrorStatus.OK;
                    }
                });
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
