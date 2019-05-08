package com.rbkmoney.kafka.common.exception;

public class RetryableException extends RuntimeException {

    public RetryableException() {
    }

    public RetryableException(String message, Throwable cause) {
        super(message, cause);
    }

    public RetryableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public RetryableException(String message) {
        super(message);
    }

    public RetryableException(Throwable cause) {
        super(cause);
    }

}
