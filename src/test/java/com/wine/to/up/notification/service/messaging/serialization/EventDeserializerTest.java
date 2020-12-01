package com.wine.to.up.notification.service.messaging.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.wine.to.up.notification.service.api.message.KafkaMessageSentEventOuterClass;
import org.junit.Test;

public class EventDeserializerTest {
    @Test
    public void deserializeTest() {
        KafkaMessageSentEventOuterClass.KafkaMessageSentEvent kafkaMessageSentEvent = KafkaMessageSentEventOuterClass
                .KafkaMessageSentEvent.newBuilder()
                .setMessage("test").build();
        byte[] rawBytes = kafkaMessageSentEvent.toByteArray();
        EventDeserializer eventDeserializer = new EventDeserializer();
        KafkaMessageSentEventOuterClass.KafkaMessageSentEvent event = eventDeserializer.deserialize("test", rawBytes);
        assertEquals("test", event.getMessage());
        eventDeserializer.close();
    }
}
