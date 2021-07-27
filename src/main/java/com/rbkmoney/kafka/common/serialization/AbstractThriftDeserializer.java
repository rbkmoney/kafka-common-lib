package com.rbkmoney.kafka.common.serialization;

import com.rbkmoney.kafka.common.exception.KafkaSerializationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.thrift.TBase;
import org.apache.thrift.TDeserializer;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractThriftDeserializer<T extends TBase> implements Deserializer<T> {

    protected final ThreadLocal<TDeserializer> thriftDeserializer = ThreadLocal.withInitial(TDeserializer::new);

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Map<String, ?> filtered = configs.entrySet().stream()
                .filter(entry -> !entry.getKey().contains("ssl"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        log.warn("AbstractThriftDeserializer configure configs: {} isKey: {} is do nothing!", filtered, isKey);
    }

    @Override
    public void close() {
        thriftDeserializer.remove();
    }

    protected T deserialize(byte[] data, T t) {
        try {
            thriftDeserializer.get().deserialize(t, data);
        } catch (Exception e) {
            log.error("Error when deserialize data", e);
            throw new KafkaSerializationException(e);
        }
        return t;
    }
}
