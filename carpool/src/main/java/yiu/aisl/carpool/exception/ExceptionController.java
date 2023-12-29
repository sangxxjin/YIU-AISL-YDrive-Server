package yiu.aisl.carpool.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionController {
    @ExceptionHandler({CustomException.class})
    protected ResponseEntity handleCustomException(CustomException ex) {
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity handleServerException(Exception ex) {
        ErrorResponse response = new ErrorResponse(ErrorCode.INSUFFICIENT_DATA);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
