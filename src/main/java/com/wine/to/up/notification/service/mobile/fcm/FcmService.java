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
    public void sendMessage(FcmPushNotificationRequest request)
            throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessage(request);
        String response = sendAndGetResponse(message);
        log.debug("Sent message without data. " + response);
    }

    private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }

    private Message getPreconfiguredMessage(FcmPushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request)
                //.setToken("AAAAro0ss70:APA91bFXxxsSuLIlAc9_q5Uil0GgxdIcdEaL126vsu0yTZ2y6df0vNNZLe-iPQ2HHzSBD6RE7ekUzzBDwWoqgusFreDp7I8LceAa320NvhX9CgBJTieQSrbTSOzaLEJBaFyUFaoBKji9")
                .setTopic("catalog")
                .build();
    }

    private Message.Builder getPreconfiguredMessageBuilder(FcmPushNotificationRequest request) {
        AndroidConfig androidConfig = getAndroidConfig();
        return Message.builder()
                .setAndroidConfig(androidConfig).setNotification(
                        new Notification(request.getTitle(), request.getMessage()));
    }
}
