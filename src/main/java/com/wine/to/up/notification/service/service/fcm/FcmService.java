package com.wine.to.up.notification.service.service.fcm;

import java.time.Duration;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.wine.to.up.notification.service.domain.model.fcm.FcmPushNotificationRequest;
import org.springframework.stereotype.Service;


@Service
public class FcmService {
    

    private Message.Builder getMessageBuilder(FcmPushNotificationRequest request) {
        AndroidConfig config = getAndroidConfig();
        return Message.builder()
            .setAndroidConfig(config)
            .setNotification(new Notification(request.getTitle(), request.getMessage()));
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
