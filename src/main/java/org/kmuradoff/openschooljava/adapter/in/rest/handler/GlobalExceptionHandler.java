package org.kmuradoff.openschooljava.adapter.in.rest.handler;

import org.kmuradoff.openschooljava.adapter.common.exception.CommonAdapterException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CommonAdapterException.class)
    public ResponseEntity<String> handleAnyException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal server error: " + e.getMessage());
    }
}
