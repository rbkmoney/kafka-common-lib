package com.rbkmoney.kafka.common.deserializer;


import com.rbkmoney.kafka.common.exception.KafkaSerializationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.thrift.TBase;
import org.apache.thrift.TDeserializer;

import java.util.Map;

@Slf4j
public abstract class AbstractDeserializerAdapter<T extends TBase> implements Deserializer<T> {

    protected final ThreadLocal<TDeserializer> thriftDeserializer = ThreadLocal.withInitial(TDeserializer::new);

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        log.warn("ThriftSerializer configure configs: {} isKey: {} is do nothing!", configs, isKey);
    }

    @Override
    public void close() {
        thriftDeserializer.remove();
    }

    protected T deserialize(byte[] data, T t) {
        try {
            thriftDeserializer.get().deserialize(t, data);
        } catch (Exception e) {
            log.error("Error when deserialize data: {} ", e);
            throw new KafkaSerializationException(e);
        }
        return t;
    }
}