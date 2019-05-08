package com.rbkmoney.kafka.common.exception;

public class SkippedException extends RuntimeException {

    public SkippedException() {
    }

    public SkippedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SkippedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SkippedException(String message) {
        super(message);
    }

    public SkippedException(Throwable cause) {
        super(cause);
    }

}
