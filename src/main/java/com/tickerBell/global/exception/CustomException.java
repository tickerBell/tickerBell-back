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
    // errorMessage 생성자
    public CustomException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
