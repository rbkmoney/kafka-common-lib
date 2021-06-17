package com.rbkmoney.kafka.common.retry;

import org.springframework.retry.RetryContext;
import org.springframework.retry.context.RetryContextSupport;

class SimpleRetryContext extends RetryContextSupport {
    public SimpleRetryContext(RetryContext parent) {
        super(parent);
    }
}