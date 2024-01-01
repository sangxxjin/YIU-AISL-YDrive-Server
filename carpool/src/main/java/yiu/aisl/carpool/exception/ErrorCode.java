package yiu.aisl.carpool.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ErrorCode {
    // 잘못된 요청
    INSUFFICIENT_DATA(400, ResultMessage.INSUFFICIENT_DATA),

    VALID_NOT_STUDENT_ID(401, ResultMessage.VALID_NOT_STUDENT_ID),
    VALID_NOT_PWD(401, ResultMessage.VALID_NOT_PWD),

    // 존재하지 않은 값
    MEMBER_NOT_EXIST(402, ResultMessage.MEMBER_NOT_EXIST),
    ERROR_CODE(402, ResultMessage.ERROR_CODE),

    // 데이터를 찾을 수 없음
    NOT_EXIST(404, ResultMessage.NOT_EXIST),

    // 데이터 충돌
    CONFLICT(409, ResultMessage.CONFLICT),
    // 데이터 중복
    DUPLICATE(409, ResultMessage.DUPLICATE),

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
        String VALID_NOT_STUDENT_ID = "가입하지 않은 학번";
        String VALID_NOT_PWD = "잘못된 비밀번호";
        String INSUFFICIENT_DATA = "데이터 부족";
        String MEMBER_NOT_EXIST = "존재하지 않는 사용자";
        String NOT_EXIST = "존재하지 않음";
        String ERROR_CODE = "인증코드 불일치";
        String CONFLICT = "데이터 충돌";
        String DUPLICATE = "데이터 중복";
        String INTERNAL_SERVER_ERROR = "내부 서버 오류";
    }
}
