package com.rbkmoney.retry;

import lombok.Getter;
import org.springframework.classify.BinaryExceptionClassifier;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.context.RetryContextSupport;
import org.springframework.util.ClassUtils;

import java.util.Map;

@Getter
public class InfiniteRetryPolicy implements RetryPolicy {

    private final int maxAttempts;

    private final BinaryExceptionClassifier retryableClassifier;

    public InfiniteRetryPolicy(int maxAttempts, Map<Class<? extends Throwable>, Boolean> retryableExceptions) {
        this(maxAttempts, retryableExceptions, false, false);
    }

    public InfiniteRetryPolicy(int maxAttempts, Map<Class<? extends Throwable>, Boolean> retryableExceptions,
                               boolean traverseCauses, boolean defaultValue) {
        this.maxAttempts = maxAttempts;
        this.retryableClassifier = new BinaryExceptionClassifier(retryableExceptions, defaultValue);
        this.retryableClassifier.setTraverseCauses(traverseCauses);
    }

    public BinaryExceptionClassifier getRetryableClassifier() {
        return retryableClassifier;
    }

    @Override
    public boolean canRetry(RetryContext context) {
        Throwable t = context.getLastThrowable();
        return (t == null || retryForException(t)) && (maxAttempts < 0 || context.getRetryCount() < maxAttempts);
    }

    @Override
    public RetryContext open(RetryContext parent) {
        return new SimpleRetryContext(parent);
    }

    @Override
    public void close(RetryContext context) {
    }

    @Override
    public void registerThrowable(RetryContext context, Throwable throwable) {
        ((SimpleRetryContext) context).registerThrowable(throwable);
    }

    private static class SimpleRetryContext extends RetryContextSupport {
        public SimpleRetryContext(RetryContext parent) {
            super(parent);
        }
    }

    private boolean retryForException(Throwable ex) {
        return retryableClassifier.classify(ex);
    }

    @Override
    public String toString() {
        return ClassUtils.getShortName(getClass()) + "[maxAttempts=" + maxAttempts + "]";
    }
}
