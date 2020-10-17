package com.wine.to.up.notification.service.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wine.to.up.commonlib.messaging.KafkaMessageHandler;
import com.wine.to.up.notification.service.api.message.KafkaMessageSentEventOuterClass.KafkaMessageSentEvent;
import com.wine.to.up.notification.service.domain.model.kafka.CatalogMessage;
import com.wine.to.up.notification.service.domain.model.fcm.FcmPushNotificationRequest;
import com.wine.to.up.notification.service.mobile.NotificationSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class CatalogKafkaMessageHandler implements KafkaMessageHandler<KafkaMessageSentEvent> {

    private NotificationSender notificationSender;

    @Autowired
    public CatalogKafkaMessageHandler(NotificationSender notificationSender){
        this.notificationSender=notificationSender;
    }

    @Override
    public void handle(KafkaMessageSentEvent message) {
        final CatalogMessage catalogMessage;
        try {
            catalogMessage = new ObjectMapper().readValue(message.getMessage(), CatalogMessage.class);
            log.info("Message received:{}", catalogMessage);
            notificationSender.sendMessage(new FcmPushNotificationRequest());

        } catch (JsonProcessingException e) {
            log.error("Could not parse Kafka message from Catalog Service!");
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
            log.error("Failed to send notification!");
        }

    }
}