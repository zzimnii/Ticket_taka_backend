package umc.tickettaka.payload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import umc.tickettaka.payload.exception.GeneralException;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus {

    OK(2000, HttpStatus.OK, "Ok"),

    BAD_REQUEST(4000, HttpStatus.BAD_REQUEST, "Bad request"),
    UNAUTHORIZED(4001, HttpStatus.UNAUTHORIZED, "User unauthorized"),
    VALIDATION_ERROR(4003, HttpStatus.BAD_REQUEST, "Validation error"),
    NOT_FOUND(4004, HttpStatus.NOT_FOUND, "Requested resource is not found"),

    INTERNAL_ERROR(5000, HttpStatus.INTERNAL_SERVER_ERROR, "Internal error"),
    DATA_ACCESS_ERROR(5001, HttpStatus.INTERNAL_SERVER_ERROR, "Data access error");


    private final Integer code;
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
    public String toString() {
        return String.format("%s (%d)", this.name(), this.getCode());
    }
}
