package com.rbkmoney.kafka.common.util;

import com.rbkmoney.machinegun.eventsink.MachineEvent;
import com.rbkmoney.machinegun.eventsink.SinkEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.time.Instant;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.stream.Collectors;

public class LogUtil {

    public static <T extends ConsumerRecord> String toString(T consumerRecord) {
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

    public static <T extends ConsumerRecord> String toString(List<T> consumerRecords) {
        return consumerRecords.stream()
                .map(LogUtil::toString)
                .collect(Collectors.joining(", "));
    }

    public static <K, V> String toSummaryString(List<ConsumerRecord<K, V>> records) {
        if (records.isEmpty()) {
            return "empty";
        }

        ConsumerRecord firstRecord = records.get(0);
        ConsumerRecord lastRecord = records.get(records.size() - 1);
        LongSummaryStatistics keySizeSummary = records.stream().mapToLong(ConsumerRecord::serializedKeySize).summaryStatistics();
        LongSummaryStatistics valueSizeSummary = records.stream().mapToLong(ConsumerRecord::serializedValueSize).summaryStatistics();
        return String.format("topic='%s', partition=%d, offset={%d...%d}, createdAt={%s...%s}, keySize={min=%d, max=%d, avg=%s}, valueSize={min=%d, max=%d, avg=%s}",
                firstRecord.topic(), firstRecord.partition(),
                firstRecord.offset(), lastRecord.offset(),
                Instant.ofEpochMilli(firstRecord.timestamp()), Instant.ofEpochMilli(lastRecord.timestamp()),
                keySizeSummary.getMin(), keySizeSummary.getMax(), keySizeSummary.getAverage(),
                valueSizeSummary.getMin(), valueSizeSummary.getMax(), valueSizeSummary.getAverage()
        );
    }

    public static <K, V> String toSummaryString(ConsumerRecords<K, V> records) {
        if (records.isEmpty()) {
            return "empty";
        }

        StringBuilder stringBuilder = new StringBuilder();

        records
                .partitions()
                .forEach(partition -> stringBuilder.append(toSummaryString(records.records(partition))).append("\n"));

        return stringBuilder.toString();
    }

    public static <K> String toSummaryStringWithMachineEventValues(List<ConsumerRecord<K, MachineEvent>> records) {
        String valueKeysString = records.stream().map(ConsumerRecord::value)
                .map(value -> String.format("'%s.%d'", value.getSourceId(), value.getEventId()))
                .collect(Collectors.joining(", "));
        return String.format("%s, values={%s}", toSummaryString(records), valueKeysString);
    }

    public static <K> String toSummaryStringWithSinkEventValues(List<ConsumerRecord<K, SinkEvent>> records) {
        String valueKeysString = records.stream().map(ConsumerRecord::value)
                .map(value -> String.format("'%s.%d'", value.getEvent().getSourceId(), value.getEvent().getEventId()))
                .collect(Collectors.joining(", "));
        return String.format("%s, values={%s}", toSummaryString(records), valueKeysString);
    }
}
