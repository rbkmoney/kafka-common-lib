package com.rbkmoney.kafka.common.serialization;

import com.rbkmoney.damsel.base.Content;
import org.junit.Assert;
import org.junit.Test;

public class SerializationTest {

    private final ThriftSerializer<Content> thriftSerializer = new ThriftSerializer<>();

    private class ContentDeserializer extends AbstractThriftDeserializer<Content> {

        @Override
        public Content deserialize(String s, byte[] bytes) {
            return super.deserialize(bytes, new Content());
        }
    }

    @Test
    public void serializeTest() {
        Content content = new Content();
        content.setType("type");
        content.setData("data".getBytes());

        byte[] bytes = thriftSerializer.serialize("poh", content);

        ContentDeserializer abstractDeserializerAdapter = new ContentDeserializer();

        Content pohContent = abstractDeserializerAdapter.deserialize("poh", bytes);

        Assert.assertEquals(content, pohContent);
    }
}
