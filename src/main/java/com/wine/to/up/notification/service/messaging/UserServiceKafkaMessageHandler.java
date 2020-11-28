package com.wine.to.up.notification.service.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wine.to.up.commonlib.messaging.KafkaMessageHandler;
import com.wine.to.up.notification.service.api.message.KafkaMessageSentEventOuterClass.KafkaMessageSentEvent;
import com.wine.to.up.notification.service.domain.entity.Notification;
import com.wine.to.up.notification.service.domain.model.apns.ApnsPushNotificationRequest;
import com.wine.to.up.notification.service.domain.model.fcm.FcmPushNotificationRequest;
import com.wine.to.up.notification.service.domain.util.NotificationType;
import com.wine.to.up.notification.service.mobile.NotificationSender;
import com.wine.to.up.notification.service.mobile.apns.ApnsService;
import com.wine.to.up.notification.service.mobile.apns.ApnsSettings;
import com.wine.to.up.notification.service.mobile.fcm.FcmService;
import com.wine.to.up.notification.service.repository.NotificationRepository;
import com.wine.to.up.user.service.api.UserServiceApiProperties;
import com.wine.to.up.user.service.api.dto.UserTokens;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.wine.to.up.user.service.api.dto.WinePriceUpdatedResponse;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@Slf4j
@Getter
public class UserServiceKafkaMessageHandler {

    // private final NotificationSender<FcmPushNotificationRequest> notificationSender;
    private final FcmService fcmService;
    private final ApnsService apnsService;

    @Autowired
    public UserServiceKafkaMessageHandler(FcmService fcmService, ApnsService apnsService) {
        this.fcmService = fcmService;
        this.apnsService = apnsService;
    }

    @KafkaListener(id = "user-service-topic-listener",
            topics = {"user-service-wine-price-updated-with-tokens"},
            containerFactory = "singleFactory")
    public void handle(WinePriceUpdatedResponse wineResponse) {
        log.info("Message received:{}", wineResponse);
        fcmService.sendAll(wineResponse);
    }

}
