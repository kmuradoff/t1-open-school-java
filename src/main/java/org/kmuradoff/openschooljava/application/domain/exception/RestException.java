package org.kmuradoff.openschooljava.application.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RestException extends RuntimeException {

    private final String code;
    private final HttpStatus httpStatus;

    public RestException(String message, HttpStatus httpStatus) {
        super(message);
        this.code = null;
        this.httpStatus = httpStatus;
    }
}