package com.rbkmoney.kafka.common.serialization.impl;

import com.rbkmoney.machinegun.eventsink.SinkEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SinkEventKafkaDeserializer extends KafkaDeserializer<SinkEvent> {

    @Override
    public SinkEvent deserialize(String topic, byte[] data) {
        return deserialize(topic, data, new SinkEvent());
    }
}
