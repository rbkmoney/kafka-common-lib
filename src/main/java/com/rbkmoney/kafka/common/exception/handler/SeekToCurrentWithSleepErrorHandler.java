package com.rbkmoney.kafka.common.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SeekToCurrentWithSleepErrorHandler extends SeekToCurrentErrorHandler {

    // must be less than session.timeout.ms (default value: 10s)
    private final Integer sleepTimeSeconds;

    public SeekToCurrentWithSleepErrorHandler() {
        super();
        this.sleepTimeSeconds = 5;
    }

    public SeekToCurrentWithSleepErrorHandler(int sleepTimeSeconds, int maxFailures) {
        super(maxFailures);
        this.sleepTimeSeconds = sleepTimeSeconds;
    }

    @Override
    public void handle(Exception thrownException, List<ConsumerRecord<?, ?>> records, Consumer<?, ?> consumer, MessageListenerContainer container) {
        log.error("Records commit failed", thrownException);

        sleepBeforeRetry();

        super.handle(thrownException, records, consumer, container);
    }

    private void sleepBeforeRetry() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(sleepTimeSeconds));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
