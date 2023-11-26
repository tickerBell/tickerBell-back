package com.tickerBell.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private ErrorCode errorCode;
    private String status;
    private String errorMessage;

    // ErrorCode 생성자
    public CustomException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
        this.status = errorCode.getStatus().toString();
        this.errorMessage = errorCode.getErrorMessage();
    }
    // ErrorCode 와 동적 errorMessage 를 받기 위한 생성자
    public CustomException(ErrorCode errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.status = errorCode.getStatus().toString();
        this.errorMessage = errorMessage;
    }
}
