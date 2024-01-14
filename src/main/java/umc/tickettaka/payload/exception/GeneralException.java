package umc.tickettaka.payload.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import umc.tickettaka.payload.BaseErrorCode;
import umc.tickettaka.payload.dto.ErrorReasonDto;
import umc.tickettaka.payload.status.ErrorStatus;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private final ErrorStatus errorStatus;
    private BaseErrorCode code;


    public GeneralException(String message) {
        super(ErrorStatus.INTERNAL_ERROR.getMessage(message));
        this.errorStatus = ErrorStatus.INTERNAL_ERROR;
    }

    public GeneralException(String message, Throwable cause) {
        super(ErrorStatus.INTERNAL_ERROR.getMessage(message), cause);
        this.errorStatus = ErrorStatus.INTERNAL_ERROR;
    }



    public GeneralException(ErrorStatus errorStatus, String message) {
        super(errorStatus.getMessage(message));
        this.errorStatus = errorStatus;
    }

    public GeneralException(ErrorStatus errorStatus, String message, Throwable cause) {
        super(errorStatus.getMessage(message), cause);
        this.errorStatus = errorStatus;
    }


    public ErrorReasonDto getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}
