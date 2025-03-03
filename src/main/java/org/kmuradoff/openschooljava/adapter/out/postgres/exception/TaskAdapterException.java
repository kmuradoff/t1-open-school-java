package org.kmuradoff.openschooljava.adapter.out.postgres.exception;

import org.kmuradoff.openschooljava.adapter.common.exception.CommonAdapterException;

public class TaskAdapterException extends CommonAdapterException {

    public TaskAdapterException(String message) {
        super(message);
    }

    public TaskAdapterException(String message, Throwable cause) {
        super(message, cause);
    }
}
