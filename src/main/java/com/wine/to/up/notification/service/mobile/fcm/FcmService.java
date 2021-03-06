package com.wine.to.up.notification.service.mobile.fcm;

import com.google.firebase.messaging.*;
import com.wine.to.up.notification.service.components.NotificationServiceMetricsCollector;
import com.wine.to.up.notification.service.domain.model.fcm.FcmPushNotificationRequest;
import com.wine.to.up.notification.service.mobile.NotificationSender;
import com.wine.to.up.user.service.api.message.WinePriceUpdatedWithTokensEventOuterClass.WinePriceUpdatedWithTokensEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Duration;
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

    private static final String DEVICE_SYSTEM = "Android";

    @Autowired
    private NotificationServiceMetricsCollector metrics;

    @Value("${app.notifications.defaults:token}")
    private String defaultToken;

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
        metrics.notificationsSentInc(DEVICE_SYSTEM);
        log.debug("Sent message without data. Token: {}, {}", request.getClientToken(), response);
    }

    @Override
    public void sendAll(WinePriceUpdatedWithTokensEvent event) {
        final String payload = "New discount on " + event.getWineName()
                + "! New price is: " + event.getNewWinePrice();
        final String message = "New price is: " + event.getNewWinePrice();
        event.getUserTokensList().forEach(t-> t.getFcmTokensList().forEach(token->{
            final FcmPushNotificationRequest fcmPushNotificationRequest=new FcmPushNotificationRequest(payload,message,token);
            try {
                sendMessage(fcmPushNotificationRequest);
            } catch (InterruptedException e) {
                log.warn("Failed to send notification!{}",fcmPushNotificationRequest.toString());
                metrics.notificationsFailedInc(DEVICE_SYSTEM);
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                log.warn("Failed to send notification!{}",fcmPushNotificationRequest.toString());
                metrics.notificationsFailedInc(DEVICE_SYSTEM);
            }
        }));
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
