package com.rbkmoney.kafka.common.exception;

public class KafkaSerializationException extends RuntimeException {

    public KafkaSerializationException() {
    }

    public KafkaSerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public KafkaSerializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public KafkaSerializationException(String message) {
        super(message);
    }

    public KafkaSerializationException(Throwable cause) {
        super(cause);
    }

}
