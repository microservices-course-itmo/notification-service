package com.wine.to.up.notification.service.messaging.serialization;

import com.google.protobuf.InvalidProtocolBufferException;
import com.wine.to.up.user.service.api.message.WinePriceUpdatedWithTokensEventOuterClass.WinePriceUpdatedWithTokensEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;


@Slf4j
public class WinePriceUpdatedWithTokensDeserializer implements Deserializer<WinePriceUpdatedWithTokensEvent> {
    @Override
    public WinePriceUpdatedWithTokensEvent deserialize(String topic, byte[] data) {
        try {
            log.debug("Received WinePriceUpdatedWithTokensEvent. Raw: {}", data);
            log.debug("{}", new String(data));
            return WinePriceUpdatedWithTokensEvent.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            log.error("Failed to deserialize message from topic: {}. {}", topic, e);
            return null;
        }
    }
}
