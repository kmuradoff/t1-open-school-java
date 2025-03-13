package org.kmuradoff.openschooljava.application.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RestException {

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}