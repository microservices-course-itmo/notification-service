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

    private NotificationSender<FcmPushNotificationRequest> notificationSender;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    public UserServiceKafkaMessageHandler(NotificationSender<FcmPushNotificationRequest> notificationSender) {
        this.notificationSender = notificationSender;
    }

    @KafkaListener(id = "user-service-topic-listener", topics = {"wine-response-topic"}, containerFactory = "singleFactory")
    public void handle(WinePriceUpdatedResponse wineResponse) {
        try {
            log.info("Message received:{}", wineResponse);
            this.sendDiscountNotifications(wineResponse);
        }
        catch (InterruptedException ex) {
            log.error("Failed to send notification", ex);
            Thread.currentThread().interrupt();
        } catch (ExecutionException ex) {
            log.error("Failed to send notification", ex);
        }
    }

    private void sendDiscountNotifications(WinePriceUpdatedResponse wineResponse) throws ExecutionException, InterruptedException {
        String payload = "New discount on " + wineResponse.getWineName() + "! New price is: " + wineResponse.getNewWinePrice();
        String message = "New price is: " + wineResponse.getNewWinePrice();

        for (UserTokens userTokens : wineResponse.getUserTokens()) {
            ApnsService apnsService = new ApnsService(new ApnsSettings());
            FcmService fcmService = new FcmService();
            if (!userTokens.getIosTokens().isEmpty()) {
                for (String token : userTokens.getIosTokens()) {
                    Notification notification = Notification.builder()
                            .message(payload)
                            .type(NotificationType.WINE_PRICE_UPDATED)
                            .wineId(Long.parseLong(wineResponse.getWineId()))
                            .userId(userTokens.getUserId())
                            .build();
                    notification.setCurrentTime();
                    this.notificationRepository.save(notification);

                    apnsService.sendMessage(new ApnsPushNotificationRequest(token, "default", payload));
                }
            }

            if (!userTokens.getFcmTokens().isEmpty()) {
                for (String token : userTokens.getFcmTokens()) {
                    Notification notification = Notification.builder()
                            .message(message)
                            .type(NotificationType.WINE_PRICE_UPDATED)
                            .wineId(Long.parseLong(wineResponse.getWineId()))
                            .userId(userTokens.getUserId())
                            .build();
                    notification.setCurrentTime();
                    this.notificationRepository.save(notification);

                    fcmService.sendMessage(new FcmPushNotificationRequest("New discount on " + wineResponse.getWineName() + "!", message, token));
                }
            }
        }
    }
}
