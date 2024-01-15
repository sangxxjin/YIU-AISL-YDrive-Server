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
    INVALID_EMAIL_VERIFICATION_CODE(401, ResultMessage.INVALID_EMAIL_VERIFICATION_CODE),
    Application_Deadline(401, ResultMessage.Application_Deadline),

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

    Number_Of_Applications_Exceeded(402, ResultMessage.Number_Of_Applications_Exceeded),
    Already_Applied(403, ResultMessage.Already_Applied),

    Already_Accept(403, ResultMessage.Already_Accept),
    Post_Written_By_Me(405, ResultMessage.Post_Written_By_Me),

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
        String Number_Of_Applications_Exceeded = "신청 인원이 초과됨";
        String Application_Deadline = "신청 마감";
        String Already_Applied = "이미 신청함";
        String Already_Accept = "이미 수락함";
        String Post_Written_By_Me = "본인이 작성한 게시물임";
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
