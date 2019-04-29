package com.rbkmoney.kafka.common.converter;

import com.rbkmoney.damsel.payment_processing.EventPayload;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class BinaryConverterImplTest {

    @Test
    public void convert() throws TException {
        BinaryConverterImpl binaryConverter = new BinaryConverterImpl();
        EventPayload expected = new EventPayload();
        expected.setInvoiceChanges(Collections.emptyList());
        EventPayload actual = binaryConverter.convert(new TSerializer().serialize(expected), EventPayload.class);
        assertEquals(expected, actual);
    }
}
