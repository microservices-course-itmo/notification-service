package com.wine.to.up.notification.service.messaging.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import com.wine.to.up.user.service.api.message.WinePriceUpdatedWithTokensEventOuterClass.WinePriceUpdatedWithTokensEvent;
import org.junit.Test;

public class WinePriceUpdatedWithTokensDeserializerTest {
    @Test
    public void testDeserializer() {
        WinePriceUpdatedWithTokensEvent event = WinePriceUpdatedWithTokensEvent.newBuilder()
            .setWineName("test")
            .build();
        byte[] rawBytes = event.toByteArray();

        WinePriceUpdatedWithTokensDeserializer deserializer = new WinePriceUpdatedWithTokensDeserializer();
        WinePriceUpdatedWithTokensEvent deserializedEvent = 
            deserializer.deserialize("user-service-wine-price-updated-with-tokens", rawBytes);
        
        assertEquals("test", deserializedEvent.getWineName());

        deserializer.close();
    }

    @Test
    public void testDeserializerError() {
        byte[] invalidBytes = "I like trains".getBytes();

        WinePriceUpdatedWithTokensDeserializer deserializer = new WinePriceUpdatedWithTokensDeserializer();
        WinePriceUpdatedWithTokensEvent deserializedEvent = 
            deserializer.deserialize("user-service-wine-price-updated-with-tokens", invalidBytes);

        assertNull(deserializedEvent);

        deserializer.close();
    }

}
