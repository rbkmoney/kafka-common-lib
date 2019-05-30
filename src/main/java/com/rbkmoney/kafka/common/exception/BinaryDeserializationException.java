package com.rbkmoney.kafka.common.exception;

public class BinaryDeserializationException extends RuntimeException {

    public BinaryDeserializationException() {
    }

    public BinaryDeserializationException(String message) {
        super(message);
    }

    public BinaryDeserializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BinaryDeserializationException(Throwable cause) {
        super(cause);
    }

    public BinaryDeserializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
