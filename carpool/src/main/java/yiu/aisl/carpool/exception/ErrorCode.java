package yiu.aisl.carpool.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ErrorCode {
    // 잘못된 요청
    INSUFFICIENT_DATA(400, ResultMessage.INSUFFICIENT_DATA),

    VALID_NOT_STUDENT_ID(401, ResultMessage.VALID_NOT_STUDENT_ID),
    INVALID_MEMBER_NUM(401, ResultMessage.INVALID_MEMBER_NUM),
    VALID_NOT_PWD(401, ResultMessage.VALID_NOT_PWD),
    VALID_EMAIL_LENGTH(401, ResultMessage.VALID_EMAIL_LENGTH),
    INVALID_EMAIL_VERIFICATION_CODE(401, ResultMessage.INVALID_EMAIL_VERIFICATION_CODE),
    APPLICATION_DEADLINE(401, ResultMessage.APPLICATION_DEADLINE),

    // 존재하지 않은 값
    MEMBER_NOT_EXIST(402, ResultMessage.MEMBER_NOT_EXIST),
    ERROR_CODE(402, ResultMessage.ERROR_CODE),

    // 데이터를 찾을 수 없음
    NOT_EXIST(404, ResultMessage.NOT_EXIST),
    CAR_NOT_EXIST(404, ResultMessage.CAR_NOT_EXIST),

    // 데이터 충돌
    CONFLICT(409, ResultMessage.CONFLICT),
    // 데이터 중복
    DUPLICATE(409, ResultMessage.DUPLICATE),

    NUMBER_OF_APPLICATIONS_EXCEEDED(402, ResultMessage.NUMBER_OF_APPLICATIONS_EXCEEDED),
    ALREADY_APPLIED(403, ResultMessage.ALREADY_APPLIED),
    CANNOT_DELETE_CARPOOL_HOUR(403, ResultMessage.CANNOT_DELETE_CARPOOL_HOUR),
    CHANGE_TO_DRIVER_MODE_REQUIRED(403, ResultMessage.CHANGE_TO_DRIVER_MODE_REQUIRED),
    CANNOT_DELETE_CARPOOL_WITH_WAITING(403, ResultMessage.CANNOT_DELETE_CARPOOL_WITH_WAITING),
    REVIEW_WRITE_NOT_ALLOWED(403, ResultMessage.REVIEW_WRITE_NOT_ALLOWED),

    ALREADY_ACCEPT(403, ResultMessage.ALREADY_ACCEPT),
    POST_WRITTEN_BY_ME(405, ResultMessage.POST_WRITTEN_BY_ME),

    // 서버 오류
    INTERNAL_SERVER_ERROR(500, ResultMessage.INTERNAL_SERVER_ERROR)
    ;



    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public interface ResultMessage {
        String REVIEW_WRITE_NOT_ALLOWED = "리뷰를 작성할 수 없습니다.";
        String CANNOT_DELETE_CARPOOL_WITH_WAITING = "대기중이거나 수락한 게스트가 있어서 삭제할 수 없습니다.";
        String CANNOT_DELETE_CARPOOL_HOUR = "출발 12시간 전에는 삭제할 수 없습니다.";
        String CHANGE_TO_DRIVER_MODE_REQUIRED = "차주 모드로 변경해야 게시글 작성이 가능합니다.";
        String INVALID_MEMBER_NUM = "신청할 수 있는 인원은 1명 이상이어야 합니다.";
        String VALID_EMAIL_LENGTH = "올바르지 않은 이메일 길이";
        String NUMBER_OF_APPLICATIONS_EXCEEDED = "신청 인원이 초과됨";
        String APPLICATION_DEADLINE = "신청 마감";
        String ALREADY_APPLIED = "이미 신청함";
        String ALREADY_ACCEPT = "이미 수락함";
        String POST_WRITTEN_BY_ME = "본인이 작성한 게시물임";
        String VALID_NOT_STUDENT_ID = "가입하지 않은 학번";
        String VALID_NOT_PWD = "잘못된 비밀번호";
        String INVALID_EMAIL_VERIFICATION_CODE = "이메일 인증 코드가 올바르지 않음";
        String INSUFFICIENT_DATA = "데이터 부족";
        String MEMBER_NOT_EXIST = "존재하지 않는 사용자";
        String CAR_NOT_EXIST = "차량을 보유하지 않음";
        String NOT_EXIST = "존재하지 않음";
        String ERROR_CODE = "인증코드 불일치";
        String CONFLICT = "데이터 충돌";
        String DUPLICATE = "데이터 중복";
        String INTERNAL_SERVER_ERROR = "내부 서버 오류";
    }
}
