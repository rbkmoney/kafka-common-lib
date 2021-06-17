package com.rbkmoney.kafka.common.loader;

import com.rbkmoney.kafka.common.exception.RetryableException;
import com.rbkmoney.kafka.common.exception.SkippedException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.common.TopicPartition;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.backoff.ExponentialBackOff;

import java.util.*;

public class PreloadListenerImplTest {

    private static final String TOPIC = "the_topic";
    private static final int PARTITION = 0;
    private MockConsumer<String, String> mockConsumer = new MockConsumer<>(OffsetResetStrategy.EARLIEST);
    private PreloadListenerImpl<String, String> preloadListener = new PreloadListenerImpl<>(
            3,
            new ExponentialBackOff(100L, 1.5)
    );

    @Test
    public void preloadToLastOffsetInPartition() {
        List<String> recordsValue = initMockConsumer();
        List<String> resultList = new ArrayList<>();

        preloadListener.preloadToLastOffsetInPartition(mockConsumer, TOPIC, PARTITION, resultList::add);

        Assert.assertEquals(recordsValue, resultList);
    }

    @Test(expected = RuntimeException.class)
    public void preloadToLastOffsetInPartitionRetryableException() {
        mockConsumer.assign(Collections.singletonList(new TopicPartition(TOPIC, 0)));

        HashMap<TopicPartition, Long> beginningOffsets = new HashMap<>();
        beginningOffsets.put(new TopicPartition(TOPIC, 0), 0L);
        mockConsumer.updateBeginningOffsets(beginningOffsets);
        HashMap<TopicPartition, Long> endOffsets = new HashMap<>();
        endOffsets.put(new TopicPartition(TOPIC, 0), 3L);
        mockConsumer.updateEndOffsets(endOffsets);

        for (int i = 0; i < 4; i++) {
            mockConsumer.schedulePollTask(() -> {
                mockConsumer.addRecord(new ConsumerRecord<>(TOPIC, 0, 0, "1", "test"));
            });
        }

        preloadListener.preloadToLastOffsetInPartition(mockConsumer, TOPIC, PARTITION, s -> {
            throw new RetryableException();
        });
    }


    @Test
    public void preloadToLastOffsetInPartitionSkippedException() {
        initMockConsumer();
        List<String> resultList = new ArrayList<>();

        preloadListener.preloadToLastOffsetInPartition(mockConsumer, TOPIC, PARTITION, s -> {
            throw new SkippedException();
        });

        Assert.assertEquals(new ArrayList<>(), resultList);
    }


    private List<String> initMockConsumer() {
        mockConsumer.assign(Collections.singletonList(new TopicPartition(TOPIC, 0)));

        HashMap<TopicPartition, Long> beginningOffsets = new HashMap<>();
        beginningOffsets.put(new TopicPartition(TOPIC, 0), 0L);
        mockConsumer.updateBeginningOffsets(beginningOffsets);
        List<String> recordsValue = Arrays.asList("myvalue0", "myvalue1", "myvalue2");
        for (int i = 0; i < recordsValue.size(); i++) {
            mockConsumer.addRecord(new ConsumerRecord<>(TOPIC, 0, i, "" + i, recordsValue.get(i)));
        }
        HashMap<TopicPartition, Long> endOffsets = new HashMap<>();
        endOffsets.put(new TopicPartition(TOPIC, 0), 3L);
        mockConsumer.updateEndOffsets(endOffsets);
        return recordsValue;
    }
}