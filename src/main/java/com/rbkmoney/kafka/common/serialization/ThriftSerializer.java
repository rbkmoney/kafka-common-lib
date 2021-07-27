package com.rbkmoney.kafka.common.serialization;

import com.rbkmoney.kafka.common.exception.KafkaSerializationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ThriftSerializer<T extends TBase> implements Serializer<T> {

    private final ThreadLocal<TSerializer> thriftSerializer = ThreadLocal.withInitial(TSerializer::new);

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Map<String, ?> filtered = configs.entrySet().stream()
                .filter(entry -> !entry.getKey().contains("ssl"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        log.warn("ThriftSerializer configure configs: {} isKey: {} is do nothing!", filtered, isKey);
    }

    @Override
    public byte[] serialize(String topic, T data) {
        log.debug("Serialize message, topic: {}, data: {}", topic, data);
        try {
            return thriftSerializer.get().serialize(data);
        } catch (TException e) {
            log.error("Error when serialize data: {} ", data, e);
            throw new KafkaSerializationException(e);
        }
    }

    @Override
    public void close() {
        thriftSerializer.remove();
    }
}
