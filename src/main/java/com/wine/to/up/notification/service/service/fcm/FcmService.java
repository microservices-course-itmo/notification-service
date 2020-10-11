package com.wine.to.up.notification.service.service.fcm;

import com.google.firebase.messaging.*;
import com.wine.to.up.notification.service.domain.model.fcm.FcmPushNotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class FcmService {

    private AndroidConfig getAndroidConfig() {
        return AndroidConfig.builder()
                .setTtl(Duration.ofMinutes(2).toMillis())
                .setPriority(AndroidConfig.Priority.NORMAL)
                .setNotification(AndroidNotification.builder()
                        .build()
                )
                .build();
    }


    public void sendMessage(FcmPushNotificationRequest request)
            throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessage(request);
        String response = sendAndGetResponse(message);
        log.debug("Sent message without data. Token: " + request.getClientToken() + ", " + response);
    }

    private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }

    private Message getPreconfiguredMessage(FcmPushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).setToken(request.getClientToken())
                .build();
    }

    private Message.Builder getPreconfiguredMessageBuilder(FcmPushNotificationRequest request) {
        AndroidConfig androidConfig = getAndroidConfig();
        return Message.builder()
                .setAndroidConfig(androidConfig).setNotification(
                        new Notification(request.getTitle(), request.getMessage()));
    }
}
