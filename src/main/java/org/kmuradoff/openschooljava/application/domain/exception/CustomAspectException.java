package org.kmuradoff.openschooljava.application.domain.exception;

public class CustomAspectException extends RuntimeException {

    public CustomAspectException(String message) {
        super(message);
    }

    public CustomAspectException(String message, Throwable cause) {
        super(message, cause);
    }
}