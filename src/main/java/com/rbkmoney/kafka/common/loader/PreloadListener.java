package com.rbkmoney.kafka.common.loader;

import org.apache.kafka.clients.consumer.Consumer;

public interface PreloadListener<K, T> {

    void preloadToLastOffsetInPartition(
            Consumer<K, T> consumer,
            String topic,
            int partition,
            java.util.function.Consumer<T> handleConsumer);

}
