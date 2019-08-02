package com.rbkmoney.kafka.common.exception.handler;

import com.rbkmoney.machinegun.eventsink.MachineEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.listener.SeekToCurrentBatchErrorHandler;

import static com.rbkmoney.kafka.common.util.LogUtil.toSummaryString;

@Slf4j
public class SeekToCurrentWithSleepBatchErrorHandler extends SeekToCurrentBatchErrorHandler {

    @Override
    public void handle(Exception thrownException, ConsumerRecords<?, ?> data, Consumer<?, ?> consumer, MessageListenerContainer container) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.error(String.format("Records commit failed, size=%d, %s", data.count(),
                toSummaryString((ConsumerRecords<String, MachineEvent>) data)), thrownException);
        super.handle(thrownException, data, consumer, container);
    }

}