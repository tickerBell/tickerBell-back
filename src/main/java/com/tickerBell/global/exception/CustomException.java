package com.tickerBell.global.exception;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustomException extends RuntimeException {
    private ErrorCode errorCode;
    private String status;
    private String errorMessage;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
        setStatus(errorCode.getStatus().toString());
        setErrorMessage(errorCode.getErrorMessage());
    }
}
