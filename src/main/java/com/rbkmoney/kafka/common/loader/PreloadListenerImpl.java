package com.rbkmoney.kafka.common.loader;

import com.rbkmoney.kafka.common.exception.RetryableException;
import com.rbkmoney.kafka.common.exception.SkippedException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.springframework.util.backoff.BackOffExecution;
import org.springframework.util.backoff.ExponentialBackOff;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Slf4j
@NoArgsConstructor
public class PreloadListenerImpl<K, T> implements PreloadListener<K, T> {

    private ThreadLocal<Integer> countAttempt = ThreadLocal.withInitial(() -> 0);
    private int maxAttempt = 3;
    private ExponentialBackOff backOff = new ExponentialBackOff();

    public PreloadListenerImpl(int maxAttempt, ExponentialBackOff backOff) {
        this.maxAttempt = maxAttempt;
        this.backOff = backOff;
    }

    @Override
    public void preloadToLastOffsetInPartition(
            Consumer<K, T> consumer,
            String topic,
            int partition,
            java.util.function.Consumer<T> handleConsumer) {
        TopicPartition topicPartition = new TopicPartition(topic, partition);
        List<TopicPartition> topics = Collections.singletonList(topicPartition);
        consumer.assign(topics);
        consumer.seekToEnd(topics);
        long current = consumer.position(topicPartition);
        if (current <= 0) {
            return;
        }
        long endOffset = current - 1;
        consumer.seekToBeginning(Collections.singletonList(topicPartition));
        current = consumer.position(topicPartition);
        BackOffExecution execution = backOff.start();
        while (current <= endOffset) {
            ConsumerRecords<K, T> records = consumer.poll(Duration.ofSeconds(1));
            try {
                if (!records.isEmpty()) {
                    records.forEach(record ->
                            safeHandle(handleConsumer, record));
                }
                current = consumer.position(topicPartition);
                execution = resetBackoff();
            } catch (RetryableException e) {
                log.error("PreloadListenerImpl preloadToLastOffsetInPartition RetryableException e: ", e);
                if (countAttempt.get() >= maxAttempt) {
                    throw new RuntimeException("PreloadListenerImpl cant retry!", e);
                }
                consumer.seek(topicPartition, current);
                countAttempt.set(countAttempt.get() + 1);
                waitBackoff(execution);
            } catch (Exception e) {
                log.error("PreloadListenerImpl preloadToLastOffsetInPartition critical exception e: ", e);
                throw e;
            }
        }
    }

    private void waitBackoff(BackOffExecution execution) {
        try {
            Thread.sleep(execution.nextBackOff());
        } catch (InterruptedException ex) {
            log.error("PreloadListenerImpl InterruptedException when wait retry e: ", ex);
            Thread.currentThread().interrupt();
        }
    }

    private BackOffExecution resetBackoff() {
        countAttempt.set(0);
        return backOff.start();
    }

    private void safeHandle(final java.util.function.Consumer<T> handleConsumer, ConsumerRecord<K, T> record) {
        try {
            handleConsumer.accept(record.value());
        } catch (RetryableException e) {
            log.error("PreloadListenerImpl preloadToLastOffsetInPartition RetryableException e: ", e);
            throw e;
        } catch (SkippedException e) {
            log.error("PreloadListenerImpl preloadToLastOffsetInPartition SkippedException e: ", e);
        }
    }

}
