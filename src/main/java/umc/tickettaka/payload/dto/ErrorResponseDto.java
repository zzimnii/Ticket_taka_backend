package umc.tickettaka.payload.dto;

import umc.tickettaka.payload.ErrorStatus;

public class ErrorResponseDto extends ResponseDto {

    private ErrorResponseDto(ErrorStatus errorErrorStatus) {
        super(false, errorErrorStatus.getCode(), errorErrorStatus.getMessage());
    }

    private ErrorResponseDto(ErrorStatus errorErrorStatus, Exception e) {
        super(false, errorErrorStatus.getCode(), errorErrorStatus.getMessage(e));
    }

    private ErrorResponseDto(ErrorStatus errorErrorStatus, String message) {
        super(false, errorErrorStatus.getCode(), errorErrorStatus.getMessage(message));
    }


    public static ErrorResponseDto of(ErrorStatus errorErrorStatus) {
        return new ErrorResponseDto(errorErrorStatus);
    }

    public static ErrorResponseDto of(ErrorStatus errorErrorStatus, Exception e) {
        return new ErrorResponseDto(errorErrorStatus, e);
    }

    public static ErrorResponseDto of(ErrorStatus errorErrorStatus, String message) {
        return new ErrorResponseDto(errorErrorStatus, message);
    }
}
