package com.wine.to.up.notification.service.messaging;

import com.wine.to.up.notification.service.domain.model.fcm.FcmPushNotificationRequest;
import com.wine.to.up.notification.service.domain.model.kafka.CatalogMessage;
import com.wine.to.up.notification.service.mobile.NotificationSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class CatalogKafkaMessageHandler {

    private NotificationSender notificationSender;

    @Autowired
    public CatalogKafkaMessageHandler(NotificationSender notificationSender) {
        this.notificationSender = notificationSender;
    }

    @KafkaListener(id = "notification-service-catalog-topic-listener", topics = {"catalog"}, containerFactory = "singleFactory")
    public void handle(CatalogMessage catalogMessage) {
        log.info("Message received:{}", catalogMessage.toString());
        try {
            notificationSender.sendMessage(new FcmPushNotificationRequest(catalogMessage.getTitle(), Long.toString(catalogMessage.getId()), "token"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
