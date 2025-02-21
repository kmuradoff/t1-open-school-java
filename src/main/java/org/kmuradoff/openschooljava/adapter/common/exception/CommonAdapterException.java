package org.kmuradoff.openschooljava.adapter.common.exception;


public class CommonAdapterException extends RuntimeException {
    public CommonAdapterException(String message) {
        super(message);
    }

    public CommonAdapterException(String message, Throwable cause) {
        super(message, cause);
    }
}
