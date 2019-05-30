package com.rbkmoney.kafka.common.serialization;

import com.rbkmoney.damsel.base.Content;
import org.junit.Assert;
import org.junit.Test;

public class SerializationTest {

    private final KafkaSerializer<Content> thriftSerializer = new KafkaSerializer<>();

    private class ContentKafkaDeserializer extends KafkaDeserializer<Content> {

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

        ContentKafkaDeserializer abstractDeserializerAdapter = new ContentKafkaDeserializer();

        Content pohContent = abstractDeserializerAdapter.deserialize("poh", bytes);

        Assert.assertEquals(content, pohContent);
    }
}