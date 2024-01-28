package umc.tickettaka.payload.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import umc.tickettaka.payload.BaseErrorCode;
import umc.tickettaka.payload.dto.ErrorReasonDto;
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
    MEMBER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4002", "Member already exists"),
    MEMBER_WRONG_INFORMATION(HttpStatus.BAD_REQUEST, "MEMBER4004", "Member information you entered is not valid."),
    TEAM_ALREADY_ASSIGNED(HttpStatus.BAD_REQUEST,"MEMBER4005", "Team is already assigned to the member"),
    USERNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "MEMBER4006", "Username already exists"),
    PASSWORD_SAME(HttpStatus.BAD_REQUEST, "MEMBER4007", "password is same"),


    //MEMBER_TOKEN Error
    INVALID_TOKEN(HttpStatus.BAD_REQUEST,"TOKEN4001", "Invalid token"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED,"TOKEN4002", "Token has expired"),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND,"TOKEN4003", "Token not found"),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED,"TOKEN4004", "Unauthorized access, token is not valid for this operation"),
    //TICKET Error
    INVALID_TICKET(HttpStatus.BAD_REQUEST,"TICKET4000","Invalid ticket creation request"),
    TICKET_NOT_FOUND(HttpStatus.NOT_FOUND,"TICKET4001","Ticket Not found"),
    TICKET_TIME_ERROR(HttpStatus.BAD_REQUEST, "TICKET4002", "Error assigning worker or reviewer to the ticket"),
    INVALID_TICKET_TIME(HttpStatus.BAD_REQUEST, "TICKET4003", "end time is faster than start time"),
    //TIMELINE Error
    TIMELINE_NOT_FOUND(HttpStatus.NOT_FOUND,"TIMELINE4000","Timeline Not found"),
    TIMELINE_NAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "TIMELINE4001", "Name hasn't entered"),
    INVALID_PROJECT_ASSOCIATION(HttpStatus.BAD_REQUEST, "TIMELINE4002", "Invalid project association for the timeline"),
    //PROJECT Error
    PROJECT_NAME_NOT_EXIST(HttpStatus.BAD_REQUEST,"PROJECT4000","Name hasn't entered"),
    INVALID_TEAM_ASSOCIATION(HttpStatus.BAD_REQUEST, "PROJECT4001", "Invalid team association for the project"),
    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND,"PROJECT4002","Project doesn't exist"),
    INVALID_UPDATE_INFO(HttpStatus.BAD_REQUEST,"PROJECT4003", "Update Name, Update Description hasn't entered"),
    PROJECT_IMAGE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "PROJECT5000", "Error uploading project image"),
    //TEAM Error
    TEAM_NAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "TEAM4000", "Invalid team creation request"),
    INVALID_MEMBER_ASSOCIATION(HttpStatus.BAD_REQUEST, "TEAM4001", "Invalid member association for the team"),
    INVALID_SCHEDULE_TEAM_ASSOCIATION(HttpStatus.BAD_REQUEST, "TEAM4002", "Invalid schedule team association for the team"),
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "TEAM4003", "TEAM Not found"),
    INVALID_UPDATE_TEAM(HttpStatus.BAD_REQUEST,"TEAM4004", "Update Name hasn't entered"),
    //MEMBERTEAM Error
    MEMBER_TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBERTEAM4000", "MEMBERTEAM Not found"),
    MEMBER_TEAM_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "MEMBERTEAM4001", "The user is already joined to the team"),
    COLOR_ALREADY_USED_IN_TEAM(HttpStatus.BAD_REQUEST, "MEMBERTEAM4002", "The color is already used"),
    //INVITATION Error
    INVITATION_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "INVITATION4000", "The user is already invited to the team"),
    INVITATION_NOT_FOUND(HttpStatus.NOT_FOUND, "INVITATION4001", "INVITAION Not found"),
    //SNS LOGIN Error
    SNS_LOGIN_WRONG_INFORMATION(HttpStatus.BAD_REQUEST, "SNS4003", "SNS information you entered is not valid."),
    //FEEABACK Error
    FEEDBACK_NOT_FOUND(HttpStatus.NOT_FOUND,"FEEDBACK4000","Feedback doesn't exist");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


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
