package com.wine.to.up.notification.service.mobile.fcm;

import com.google.firebase.messaging.*;
import com.wine.to.up.notification.service.domain.model.fcm.FcmPushNotificationRequest;
import com.wine.to.up.notification.service.domain.util.NotificationType;
import com.wine.to.up.notification.service.mobile.NotificationSender;
import com.wine.to.up.notification.service.repository.NotificationRepository;
import com.wine.to.up.user.service.api.dto.UserTokens;
import com.wine.to.up.user.service.api.dto.WinePriceUpdatedResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A service used to send push notifications to Android devices
 * through Firebase Cloud Messaging (FCM).
 * Uses FirebaseApp initialized on startup in FcmInitializer.
 * @see com.wine.to.up.notification.service.mobile.fcm.FcmInitializer
 */

@Slf4j
@Service
public class FcmService implements NotificationSender<FcmPushNotificationRequest> {

    @Value("${app.notifications.defaults:token}")
    private String defaultToken;

    private final NotificationRepository notificationRepository;

    @Autowired
    public FcmService(NotificationRepository notificationRepository){
        this.notificationRepository=notificationRepository;
    }
    /**
     * Implementation of NotificationSender's sendMessage for FCM.
     * 
     * @param request  Data object with FCM push information
     * 
     * @throws ExecutionException
     * @throws InterruptedException
     * 
     * @see com.wine.to.up.notification.service.domain.model.fcm.FcmPushNotificationRequest
     * @see com.wine.to.up.notification.service.mobile.NotificationSender
     */
    @Override
    public void sendMessage(FcmPushNotificationRequest request)
            throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessage(request);
        String response = sendAndGetResponse(message);
        log.debug("Sent message without data. Token: {}, {}", request.getClientToken(), response);
    }

    @Override
    public void sendAll(WinePriceUpdatedResponse winePriceUpdatedResponse) {
        final String payload = "New discount on " + winePriceUpdatedResponse.getWineName()
                + "! New price is: " + winePriceUpdatedResponse.getNewWinePrice();
        final String message = "New price is: " + winePriceUpdatedResponse.getNewWinePrice();
        winePriceUpdatedResponse.getUserTokens().forEach(t->{
            t.getFcmTokens().forEach(token->{

                com.wine.to.up.notification.service.domain.entity.Notification notification=com.wine.to.up.notification.service.domain.entity.Notification.builder()
                        .message(message)
                        .type(NotificationType.WINE_PRICE_UPDATED)
                        .wineId(Long.parseLong(winePriceUpdatedResponse.getWineId()))
                        .userId(t.getUserId())
                        .build();
                notification.setCurrentTime();
                //notificationRepository.save(notification);

                final FcmPushNotificationRequest fcmPushNotificationRequest=new FcmPushNotificationRequest(payload,message,token);
                try {
                    sendMessage(fcmPushNotificationRequest);
                } catch (InterruptedException | ExecutionException e) {
                    log.warn("Failed to send notification!{}",fcmPushNotificationRequest.toString());
                }
            });
        });
    }

    private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }

    private Message getPreconfiguredMessage(FcmPushNotificationRequest request) {
        return Message.builder()
                .setAndroidConfig(getAndroidConfig())
                .setNotification(new Notification(request.getTitle(), request.getMessage()))
                .setToken(request.getClientToken())
                //.setTopic("catalog")
                .build();
    }

    private AndroidConfig getAndroidConfig() {
        return AndroidConfig.builder()
                .setTtl(Duration.ofMinutes(2).toMillis())
                .setPriority(AndroidConfig.Priority.NORMAL)
                .setNotification(AndroidNotification.builder()
                        .build()
                )
                .build();
    }
}
