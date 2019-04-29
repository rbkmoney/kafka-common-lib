package com.rbkmoney.kafka.common.converter;

import com.rbkmoney.damsel.payment_processing.EventPayload;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;

@Slf4j
public class BinaryConverterImpl implements BinaryConverter<EventPayload> {

    ThreadLocal<TDeserializer> tDeserializerThreadLocal = ThreadLocal.withInitial(() -> new TDeserializer(new TBinaryProtocol.Factory()));

    @Override
    public EventPayload convert(byte[] bin, Class<EventPayload> clazz) {
        EventPayload event = new EventPayload();
        try {
            tDeserializerThreadLocal.get().deserialize(event, bin);
        } catch (TException e) {
            log.error("BinaryConverterImpl e: ", e);
        }
        return event;
    }

}
