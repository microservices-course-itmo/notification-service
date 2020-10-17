package com.wine.to.up.notification.service.mobile.fcm;

import com.google.firebase.messaging.*;
import com.wine.to.up.notification.service.domain.model.fcm.FcmPushNotificationRequest;
import com.wine.to.up.notification.service.mobile.NotificationSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
/**
 * A service used to send push notifications to Android devices
 * through Firebase Cloud Messaging (FCM).
 * Uses FirebaseApp initialized on startup in FcmInitializer.
 * @see com.wine.to.up.notification.service.mobile.fcm.FcmInitializer
 */
public class FcmService implements NotificationSender<FcmPushNotificationRequest> {

    private AndroidConfig getAndroidConfig() {
        return AndroidConfig.builder()
                .setTtl(Duration.ofMinutes(2).toMillis())
                .setPriority(AndroidConfig.Priority.NORMAL)
                .setNotification(AndroidNotification.builder()
                        .build()
                )
                .build();
    }

    @Override
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
    public void sendMessage(FcmPushNotificationRequest request)
            throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessage(request);
        String response = sendAndGetResponse(message);
        log.debug("Sent message without data. Token: " + request.getClientToken() + ", " + response);
    }

    /**
     * Sends message to FCM, blocking until getting a response.
     * 
     * @param message  Firebase message object to be sent
     * @return  A string response from FCM
     * 
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }

    /**
     * Builds FCM Message object using passed FcmPushNotificationRequest.
     * 
     * @param request  Data object with FCM push information
     * @return  FCM Message object
     */
    private Message getPreconfiguredMessage(FcmPushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).setToken(request.getClientToken())
                .build();
    }

    /**
     * Configures Firebase's Message builder using passed FcmPushNotificationRequest.
     * Because Firebase loves building stuff.
     * 
     * @param request  Data object with FCM push information
     * @return  Configured Message builder
     */
    private Message.Builder getPreconfiguredMessageBuilder(FcmPushNotificationRequest request) {
        AndroidConfig androidConfig = getAndroidConfig();
        return Message.builder()
                .setAndroidConfig(androidConfig).setNotification(
                        new Notification(request.getTitle(), request.getMessage()));
    }
}
