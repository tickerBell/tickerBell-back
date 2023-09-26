package com.tickerBell.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private ErrorCode errorCode;
    private String status;
    private String errorMessage;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
        this.status = errorCode.getStatus().toString();
        this.errorMessage = errorCode.getErrorMessage();
    }
}
