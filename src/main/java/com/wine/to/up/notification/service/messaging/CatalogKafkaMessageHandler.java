package com.wine.to.up.notification.service.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wine.to.up.commonlib.messaging.KafkaMessageHandler;
import com.wine.to.up.notification.service.api.message.KafkaMessageSentEventOuterClass.KafkaMessageSentEvent;
import com.wine.to.up.notification.service.domain.entity.CatalogMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CatalogKafkaMessageHandler implements KafkaMessageHandler<KafkaMessageSentEvent> {

    @Override
    public void handle(KafkaMessageSentEvent message) {
        final CatalogMessage catalogMessage;
        try {
            catalogMessage = new ObjectMapper().readValue(message.getMessage(), CatalogMessage.class);
            log.info("Message received:{}", catalogMessage);
        } catch (JsonProcessingException e) {
            log.error("Could not parse Kafka message from Catalog Service!");
        }

    }
}