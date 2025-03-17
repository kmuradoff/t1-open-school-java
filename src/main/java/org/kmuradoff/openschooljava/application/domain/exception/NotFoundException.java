package org.kmuradoff.openschooljava.application.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RestException {

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}