package com.rbkmoney.kafka.common.serialization.impl;

import com.rbkmoney.kafka.common.exception.BinaryDeserializationException;
import com.rbkmoney.kafka.common.serialization.BinaryDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TBase;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;

@Slf4j
public abstract class ThriftDeserializer<T extends TBase> implements BinaryDeserializer<T> {

    private final ThreadLocal<TDeserializer> thriftDeserializer = ThreadLocal.withInitial(TDeserializer::new);

    protected T deserialize(byte[] bin, T t) {
        log.debug("Deserialize, bin[]: {}", bin.length);
        try {
            thriftDeserializer.get().deserialize(t, bin);
        } catch (TException e) {
            log.error("Error when deserialize data", e);
            throw new BinaryDeserializationException(e);
        }
        return t;
    }
}
