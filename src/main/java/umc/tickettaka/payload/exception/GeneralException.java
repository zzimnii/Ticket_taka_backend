package umc.tickettaka.payload.exception;

import lombok.Getter;
import umc.tickettaka.payload.ErrorStatus;

@Getter
public class GeneralException extends RuntimeException {

    private final ErrorStatus errorErrorStatus;

    public GeneralException() {
        super(ErrorStatus.INTERNAL_ERROR.getMessage());
        this.errorErrorStatus = ErrorStatus.INTERNAL_ERROR;
    }

    public GeneralException(String message) {
        super(ErrorStatus.INTERNAL_ERROR.getMessage(message));
        this.errorErrorStatus = ErrorStatus.INTERNAL_ERROR;
    }

    public GeneralException(String message, Throwable cause) {
        super(ErrorStatus.INTERNAL_ERROR.getMessage(message), cause);
        this.errorErrorStatus = ErrorStatus.INTERNAL_ERROR;
    }

    public GeneralException(Throwable cause) {
        super(ErrorStatus.INTERNAL_ERROR.getMessage(cause));
        this.errorErrorStatus = ErrorStatus.INTERNAL_ERROR;
    }

    public GeneralException(ErrorStatus errorErrorStatus) {
        super(errorErrorStatus.getMessage());
        this.errorErrorStatus = errorErrorStatus;
    }

    public GeneralException(ErrorStatus errorErrorStatus, String message) {
        super(errorErrorStatus.getMessage(message));
        this.errorErrorStatus = errorErrorStatus;
    }

    public GeneralException(ErrorStatus errorErrorStatus, String message, Throwable cause) {
        super(errorErrorStatus.getMessage(message), cause);
        this.errorErrorStatus = errorErrorStatus;
    }

    public GeneralException(ErrorStatus errorErrorStatus, Throwable cause) {
        super(errorErrorStatus.getMessage(cause), cause);
        this.errorErrorStatus = errorErrorStatus;
    }
}
