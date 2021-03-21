package com.wine.to.up.notification.service.messaging;

import com.wine.to.up.notification.service.components.NotificationServiceMetricsCollector;
import com.wine.to.up.notification.service.mobile.apns.ApnsService;
import com.wine.to.up.notification.service.mobile.expo.ExpoService;
import com.wine.to.up.notification.service.mobile.fcm.FcmService;
import com.wine.to.up.notification.service.repository.NotificationRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.wine.to.up.user.service.api.message.WinePriceUpdatedWithTokensEventOuterClass.WinePriceUpdatedWithTokensEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Getter
@RequiredArgsConstructor
public class UserServiceKafkaMessageHandler {

    private final NotificationRepository notificationRepository;

    private final FcmService fcmService;
    private final ApnsService apnsService;
    private final ExpoService expoService;

    private final NotificationServiceMetricsCollector metrics;

    @KafkaListener(id = "user-service-topic-listener",
            topics = {"user-service-wine-price-updated-with-tokens"},
            containerFactory = "singleFactory")
    public void handle(WinePriceUpdatedWithTokensEvent event) {
        log.debug("Message received:{}", event);
        metrics.messagesReceivedInc();

        notificationRepository.saveWinePriceUpdatedWithTokens(event);

        fcmService.sendAll(event);
        apnsService.sendAll(event);
        expoService.sendAll(event);
    }

}
