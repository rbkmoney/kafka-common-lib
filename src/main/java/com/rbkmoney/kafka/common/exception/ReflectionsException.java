package com.rbkmoney.kafka.common.exception;

public class ReflectionsException extends RuntimeException {

    public ReflectionsException() {
    }

    public ReflectionsException(String message) {
        super(message);
    }

    public ReflectionsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectionsException(Throwable cause) {
        super(cause);
    }

    public ReflectionsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
