package com.interbank.challenge.infrastructure.controller.exception;

import lombok.Getter;

@Getter
public class ProviderException extends RuntimeException {
    private String errorCode;

    public ProviderException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ProviderException(Throwable cause) {
        super(cause);
    }

    public ProviderException(String message, Throwable cause) {
        super(message, cause);
    }


}
