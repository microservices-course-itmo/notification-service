package com.wine.to.up.notification.service.messaging;

import com.wine.to.up.commonlib.messaging.KafkaMessageHandler;
import com.wine.to.up.notification.service.api.message.KafkaMessageSentEventOuterClass.KafkaMessageSentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CatalogKafkaMessageHandler implements KafkaMessageHandler<KafkaMessageSentEvent> {

    @Override
    public void handle(KafkaMessageSentEvent message) {
        log.info("Message received:", message);
    }
}