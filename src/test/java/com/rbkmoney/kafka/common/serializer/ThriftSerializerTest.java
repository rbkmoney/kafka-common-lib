package com.rbkmoney.kafka.common.serializer;

import com.rbkmoney.damsel.base.Content;
import com.rbkmoney.kafka.common.deserializer.AbstractDeserializerAdapter;
import org.junit.Assert;
import org.junit.Test;

public class ThriftSerializerTest {

    ThriftSerializer thriftSerializer = new ThriftSerializer();

    private class AbstractDeserializerAdapterImpl extends AbstractDeserializerAdapter<Content> {

        @Override
        public Content deserialize(String s, byte[] bytes) {
            Content content = new Content();
            return super.deserialize(bytes, content);
        }
    }

    @Test
    public void serializeTest() {
        Content content = new Content();
        content.setType("type");
        content.setData("data".getBytes());
        byte[] bytes = thriftSerializer.serialize("poh", content);

        AbstractDeserializerAdapterImpl abstractDeserializerAdapter = new AbstractDeserializerAdapterImpl();

        Content pohContent = abstractDeserializerAdapter.deserialize("poh", bytes);

        Assert.assertEquals(content, pohContent);
    }

}