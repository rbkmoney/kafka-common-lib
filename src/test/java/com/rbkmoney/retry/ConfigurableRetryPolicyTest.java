package com.rbkmoney.retry;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.retry.context.RetryContextSupport;

import java.util.HashMap;

public class ConfigurableRetryPolicyTest {

    @Test
    public void infiniteRetryPolicyTest() {
        ConfigurableRetryPolicy configurableRetryPolicy = new ConfigurableRetryPolicy(-1,
                new HashMap<Class<? extends Throwable>, Boolean>() {{ put(IllegalAccessException.class, true); }});

        Assert.assertEquals(-1, configurableRetryPolicy.getMaxAttempts());

        canRetryWithoutException(configurableRetryPolicy);

        canRetryForSupportedException(configurableRetryPolicy);

        cantRetryForNonSupportedException(configurableRetryPolicy);

        canRetryInfinitevely(configurableRetryPolicy, true);

    }

    @Test
    public void finiteRetryPolicyTest() {
        ConfigurableRetryPolicy configurableRetryPolicy = new ConfigurableRetryPolicy(2,
                new HashMap<Class<? extends Throwable>, Boolean>() {{ put(IllegalAccessException.class, true); }});

        Assert.assertEquals(2, configurableRetryPolicy.getMaxAttempts());

        canRetryWithoutException(configurableRetryPolicy);

        canRetryForSupportedException(configurableRetryPolicy);

        cantRetryForNonSupportedException(configurableRetryPolicy);

        canRetryInfinitevely(configurableRetryPolicy, false);
    }

    private void canRetryWithoutException(ConfigurableRetryPolicy configurableRetryPolicy) {
        RetryContextSupport retryContextWithoutException = new RetryContextSupport(null);
        Assert.assertTrue(configurableRetryPolicy.canRetry(retryContextWithoutException));
    }

    private void canRetryForSupportedException(ConfigurableRetryPolicy configurableRetryPolicy) {
        RetryContextSupport retryContextWithRetryableException = new RetryContextSupport(null);
        retryContextWithRetryableException.registerThrowable(new IllegalAccessException());
        Assert.assertTrue(configurableRetryPolicy.canRetry(retryContextWithRetryableException));
    }

    private void cantRetryForNonSupportedException(ConfigurableRetryPolicy configurableRetryPolicy) {
        RetryContextSupport retryContextWithNonRetryableException = new RetryContextSupport(null);
        retryContextWithNonRetryableException.registerThrowable(new RuntimeException());
        Assert.assertFalse(configurableRetryPolicy.canRetry(retryContextWithNonRetryableException));
    }

    private void canRetryInfinitevely(ConfigurableRetryPolicy configurableRetryPolicy, Boolean infinite) {
        RetryContextSupport retryContextWithEndedRetryCount = new RetryContextSupport(null);
        retryContextWithEndedRetryCount.registerThrowable(new IllegalAccessException());
        retryContextWithEndedRetryCount.registerThrowable(new IllegalAccessException());
        retryContextWithEndedRetryCount.registerThrowable(new IllegalAccessException());
        if (infinite)
            Assert.assertTrue(configurableRetryPolicy.canRetry(retryContextWithEndedRetryCount));
        else
            Assert.assertFalse(configurableRetryPolicy.canRetry(retryContextWithEndedRetryCount));
    }

}
