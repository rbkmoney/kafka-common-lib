package com.rbkmoney.kafka.common.serialization.impl;

import com.rbkmoney.damsel.payment_processing.EventPayload;

public class PaymentEventPayloadDeserializer extends ThriftDeserializer<EventPayload> {

    @Override
    public EventPayload deserialize(byte[] bin) {
        return deserialize(bin, new EventPayload());
    }
}
