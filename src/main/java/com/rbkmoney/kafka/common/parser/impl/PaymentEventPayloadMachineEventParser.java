package com.rbkmoney.kafka.common.parser.impl;

import com.rbkmoney.damsel.payment_processing.EventPayload;
import com.rbkmoney.kafka.common.serialization.BinaryDeserializer;

public class PaymentEventPayloadMachineEventParser extends MachineEventParser<EventPayload> {

    public PaymentEventPayloadMachineEventParser(BinaryDeserializer<EventPayload> deserializer) {
        super(deserializer);
    }
}
