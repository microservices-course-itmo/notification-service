package com.wine.to.up.notification.service.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wine.to.up.commonlib.messaging.KafkaMessageHandler;
import com.wine.to.up.notification.service.api.message.KafkaMessageSentEventOuterClass.KafkaMessageSentEvent;
import com.wine.to.up.notification.service.domain.model.apns.ApnsPushNotificationRequest;
import com.wine.to.up.notification.service.domain.model.kafka.CatalogMessage;
import com.wine.to.up.notification.service.domain.model.fcm.FcmPushNotificationRequest;
import com.wine.to.up.notification.service.mobile.NotificationSender;
import com.wine.to.up.notification.service.mobile.apns.ApnsService;
import com.wine.to.up.notification.service.mobile.apns.ApnsSettings;
import com.wine.to.up.notification.service.mobile.fcm.FcmService;
import com.wine.to.up.user.service.api.dto.UserTokens;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.wine.to.up.user.service.api.dto.WinePriceUpdatedResponse;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class UserServiceKafkaMessageHandler implements KafkaMessageHandler<KafkaMessageSentEvent> {

    private NotificationSender notificationSender;

    @Autowired
    public UserServiceKafkaMessageHandler(NotificationSender notificationSender) {
        this.notificationSender = notificationSender;
    }

    @Override
    @KafkaListener(id = "user-service-topic-listener", topics = {"wine-response-topic"}, containerFactory = "batchFactory")
    public void handle(KafkaMessageSentEvent message) {
        final WinePriceUpdatedResponse wineResponse;
        try {
            wineResponse = new ObjectMapper().readValue(message.getMessage(), WinePriceUpdatedResponse.class);
            log.info("Message received:{}", wineResponse);
            for (UserTokens userTokens : wineResponse.getUserTokens()) {
                ApnsService apnsService = new ApnsService(new ApnsSettings());
                FcmService fcmService = new FcmService();
                if (!userTokens.getIosTokens().isEmpty()) {
                    for (String token : userTokens.getIosTokens()) {
                        apnsService.sendMessage(new ApnsPushNotificationRequest(token, "default",
                                "New discount on " + wineResponse.getWineName() + "! New price is: " + wineResponse.getNewWinePrice()));
                    }
                }

                if (!userTokens.getFcmTokens().isEmpty()) {
                    for (String token : userTokens.getFcmTokens()) {
                        fcmService.sendMessage(new FcmPushNotificationRequest("New discount on " + wineResponse.getWineName() + "!",
                                "New price is: " + wineResponse.getNewWinePrice(), token));
                    }
                }
            }
        } catch (JsonProcessingException e) {
            log.error("Could not parse Kafka message from User Service", e);
        } catch (InterruptedException | ExecutionException ex) {
            log.error("Failed to send notification", ex);
        }

    }
}
