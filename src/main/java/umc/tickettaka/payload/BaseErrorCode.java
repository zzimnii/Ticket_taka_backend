package umc.tickettaka.payload;

import umc.tickettaka.payload.dto.ErrorReasonDto;

public interface BaseErrorCode {

    ErrorReasonDto getReason();
    ErrorReasonDto getReasonHttpStatus();
}
