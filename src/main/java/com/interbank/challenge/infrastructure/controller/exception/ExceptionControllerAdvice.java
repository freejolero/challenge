package com.interbank.challenge.infrastructure.controller.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Slf4j
@Component
@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(ProviderException.class)
    public ResponseEntity<ExceptionModel> providerException(HttpServletRequest request, ProviderException exception) {
        return handleException(request, exception.getErrorCode(), HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    private ResponseEntity<ExceptionModel> handleException(HttpServletRequest request, String errorCode, HttpStatus status, String functionalMessage) {

        ExceptionModel exceptionResponseData = ExceptionModel.builder()
                .timestamp(System.currentTimeMillis())
                .status(status.value())
                .errorCode(errorCode)
                .path(request.getRequestURI())
                .build();

        log.error("handleException-> code: {}, message: {}", errorCode, functionalMessage);

        return new ResponseEntity<>(exceptionResponseData, status);
    }

    @Getter
    @Builder
    @ToString
    private static class ExceptionModel implements Serializable {
        private long timestamp;
        private int status;
        private String errorCode;
        private String path;
    }
}
