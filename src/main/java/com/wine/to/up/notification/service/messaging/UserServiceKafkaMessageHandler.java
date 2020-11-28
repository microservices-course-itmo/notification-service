package com.wine.to.up.notification.service.messaging;

import com.wine.to.up.notification.service.components.NotificationServiceMetricsCollector;
import com.wine.to.up.notification.service.mobile.apns.ApnsService;
import com.wine.to.up.notification.service.mobile.fcm.FcmService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.wine.to.up.user.service.api.message.WinePriceUpdatedWithTokensEventOuterClass.WinePriceUpdatedWithTokensEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Getter
public class UserServiceKafkaMessageHandler {

    // private final NotificationSender<FcmPushNotificationRequest> notificationSender;
    private final FcmService fcmService;
    private final ApnsService apnsService;
    private final NotificationServiceMetricsCollector metrics;

    @Autowired
    public UserServiceKafkaMessageHandler(FcmService fcmService, ApnsService apnsService, NotificationServiceMetricsCollector metrics) {
        this.fcmService = fcmService;
        this.apnsService = apnsService;
        this.metrics = metrics;
    }

    @KafkaListener(id = "user-service-topic-listener",
            topics = {"user-service-wine-price-updated-with-tokens"},
            containerFactory = "singleFactory")
    public void handle(WinePriceUpdatedWithTokensEvent event) {
        log.info("Message received:{}", event);
        metrics.messagesReceivedInc();

        fcmService.sendAll(event);
    }

}
