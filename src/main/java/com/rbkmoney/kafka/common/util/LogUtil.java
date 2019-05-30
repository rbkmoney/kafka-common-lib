package com.rbkmoney.kafka.common.util;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;
import java.util.stream.Collectors;

public class LogUtil {

    public static String toString(ConsumerRecord consumerRecord) {
        StringBuilder sb = new StringBuilder("ConsumerRecord{");
        sb.append("topic = ").append(consumerRecord.topic());
        sb.append(", partition = ").append(consumerRecord.partition());
        sb.append(", offset = ").append(consumerRecord.offset());
        sb.append(", ").append(consumerRecord.timestampType()).append(" = ").append(consumerRecord.timestamp());
        sb.append(", serializedKeySize = ").append(consumerRecord.serializedKeySize());
        sb.append(", serializedValueSize = ").append(consumerRecord.serializedValueSize());
        sb.append(", key = ").append(consumerRecord.key());
        sb.append('}');
        return sb.toString();
    }

    public static String toString(List<ConsumerRecord> consumerRecords) {
        return consumerRecords.stream()
                .map(LogUtil::toString)
                .collect(Collectors.joining(", "));

    }

}
