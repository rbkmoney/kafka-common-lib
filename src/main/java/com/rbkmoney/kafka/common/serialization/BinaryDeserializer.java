package com.rbkmoney.kafka.common.serialization;

public interface BinaryDeserializer<T> {

    T deserialize(byte[] bin);

}
