package com.rbkmoney.kafka.common.serialization;

import com.rbkmoney.damsel.payment_processing.EventPayload;
import com.rbkmoney.kafka.common.serialization.impl.PaymentEventPayloadDeserializer;
import com.rbkmoney.kafka.common.serialization.impl.PaymentEventPayloadSerializer;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class SerializationTest {

    @Test
    public void serializationTest() {
        EventPayload expectedEventPayload = EventPayload.invoice_changes(Collections.emptyList());

        PaymentEventPayloadSerializer serializer = new PaymentEventPayloadSerializer();
        byte[] serializeEventPayload = serializer.serialize(expectedEventPayload);

        PaymentEventPayloadDeserializer deserializer = new PaymentEventPayloadDeserializer();
        EventPayload actualEventPayload = deserializer.deserialize(serializeEventPayload);

        assertEquals(expectedEventPayload, actualEventPayload);
    }
}
