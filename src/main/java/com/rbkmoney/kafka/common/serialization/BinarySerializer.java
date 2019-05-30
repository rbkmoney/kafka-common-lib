package com.rbkmoney.kafka.common.serialization;

public interface BinarySerializer<T> {

    byte[] serialize(T data);

}
