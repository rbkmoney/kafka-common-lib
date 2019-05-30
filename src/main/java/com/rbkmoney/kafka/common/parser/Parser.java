package com.rbkmoney.kafka.common.parser;

public interface Parser<F, T> {

    T parse(F data);
}
