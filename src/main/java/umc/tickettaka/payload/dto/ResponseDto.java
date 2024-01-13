package umc.tickettaka.payload.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import umc.tickettaka.payload.ErrorStatus;

@Getter
@ToString
@RequiredArgsConstructor
public class ResponseDto {

    private final Boolean success;
    private final Integer code;
    private final String message;

    public static ResponseDto of(Boolean success, ErrorStatus errorStatus) {
        return new ResponseDto(success, errorStatus.getCode(), errorStatus.getMessage());
    }

    public static ResponseDto of(Boolean success, ErrorStatus errorErrorStatus, Exception e) {
        return new ResponseDto(success, errorErrorStatus.getCode(), errorErrorStatus.getMessage(e));
    }

    public static ResponseDto of(Boolean success, ErrorStatus errorErrorStatus, String message) {
        return new ResponseDto(success, errorErrorStatus.getCode(), errorErrorStatus.getMessage(message));
    }
}
