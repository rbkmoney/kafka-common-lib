package com.rbkmoney.kafka.common.converter;

public interface BinaryConverter<T> {

    T convert(byte[] bin, Class<T> clazz);

}
