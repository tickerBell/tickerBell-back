package com.tickerBell.global.exception;

import com.tickerBell.global.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

@ControllerAdvice
@Slf4j
public class ExceptionController {


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException e, Model model) {
        log.info("핸들링한 에러 발생");
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(new Response(e.getErrorMessage(), "커스텀 예외 반환"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> processValidationError(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]");
        }
        log.info("valid 검사 에러 발생");
        CustomException e = new CustomException(ErrorCode.REQUEST_INVALID);
        return ResponseEntity.badRequest().body(new Response(e, builder.toString()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleCustomException(Exception e, Model model) {
        log.info("핸들링하지 않은 에러 발생");
        log.info("exception: " + e);
        return ResponseEntity.badRequest().body(new Response(e.getMessage()));
    }
}
