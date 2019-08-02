package com.rbkmoney.kafka.common.exception.handler;

import com.rbkmoney.machinegun.eventsink.MachineEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.listener.SeekToCurrentBatchErrorHandler;

import java.util.concurrent.TimeUnit;

import static com.rbkmoney.kafka.common.util.LogUtil.toSummaryString;

@Slf4j
public class SeekToCurrentWithSleepBatchErrorHandler extends SeekToCurrentBatchErrorHandler {

    private final Integer sleepTimeSeconds;

    public SeekToCurrentWithSleepBatchErrorHandler(Integer sleepTimeSeconds) {
        super();
        this.sleepTimeSeconds = sleepTimeSeconds;
    }

    public SeekToCurrentWithSleepBatchErrorHandler() {
        super();
        this.sleepTimeSeconds = 5;
    }

    @Override
    public void handle(Exception thrownException, ConsumerRecords<?, ?> data, Consumer<?, ?> consumer, MessageListenerContainer container) {
        log.error(String.format("Records commit failed, size=%d, %s", data.count(),
                toSummaryString((ConsumerRecords<String, MachineEvent>) data)), thrownException);

        sleepBeforeRetry();

        super.handle(thrownException, data, consumer, container);
    }

    private void sleepBeforeRetry() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(sleepTimeSeconds));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}