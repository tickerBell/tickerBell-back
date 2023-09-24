package com.tickerBell.global.exception;

import com.tickerBell.global.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

@ControllerAdvice
@Slf4j
public class ExceptionController {


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException e, Model model) {
        model.addAttribute("errorMessage", e.getErrorMessage());
        model.addAttribute("status", e.getStatus().toString());
        log.info("핸들링한 에러 발생");
        return ResponseEntity.badRequest().body(new Response(e.getErrorMessage()));
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleCustomException(Exception e, Model model) {
//        log.info("핸들링하지 않은 에러 발생");
//        log.info("exception: " + e);
//        return ResponseEntity.badRequest().body(new Response(e.getMessage()));
//    }
}
