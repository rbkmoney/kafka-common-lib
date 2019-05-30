package com.rbkmoney.kafka.common.exception;

public class BinarySerializationException extends RuntimeException {

    public BinarySerializationException() {
    }

    public BinarySerializationException(String message) {
        super(message);
    }

    public BinarySerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BinarySerializationException(Throwable cause) {
        super(cause);
    }

    public BinarySerializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
