package com.rbkmoney.kafka.common.parser.impl;

import com.rbkmoney.kafka.common.exception.ParseException;
import com.rbkmoney.kafka.common.parser.Parser;
import com.rbkmoney.kafka.common.serialization.BinaryDeserializer;
import com.rbkmoney.machinegun.eventsink.MachineEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MachineEventParser<T> implements Parser<MachineEvent, T> {

    private final BinaryDeserializer<T> deserializer;

    @Override
    public T parse(MachineEvent data) {
        try {
            byte[] bin = data.getData().getBin();
            return deserializer.deserialize(bin);
        } catch (Exception e) {
            log.error("Exception when parse message e: ", e);
            throw new ParseException(e);
        }
    }
}
