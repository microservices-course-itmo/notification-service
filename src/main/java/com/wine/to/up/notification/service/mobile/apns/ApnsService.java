package com.wine.to.up.notification.service.mobile.apns;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.ApnsClientBuilder;
import com.eatthepath.pushy.apns.PushNotificationResponse;
import com.eatthepath.pushy.apns.auth.ApnsSigningKey;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.wine.to.up.notification.service.domain.model.apns.ApnsPushNotificationRequest;
import com.wine.to.up.notification.service.mobile.NotificationSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

//@Service
@Slf4j
public class ApnsService implements NotificationSender<ApnsPushNotificationRequest> {

    private ApnsClient apnsClient;

    public ApnsService(ApnsSettings settings) {
        Path p = Paths.get(settings.getKeyFile());
        try (InputStream stream = Files.newInputStream(p)) {
            apnsClient = new ApnsClientBuilder()
                    .setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST)
                    .setSigningKey(ApnsSigningKey.loadFromInputStream(stream, settings.getTeamId(), settings.getKeyId()))
                    .build();
            log.info("Successfully initialized APNS");
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("Error initializing APNS client", e);
        }
    }

    @Override
    public void sendMessage(ApnsPushNotificationRequest request)
            throws ExecutionException, InterruptedException {
        SimpleApnsPushNotification notification = new SimpleApnsPushNotification(
                request.getDeviceToken(), request.getTopic(), request.getPayload()
        );
        PushNotificationResponse<SimpleApnsPushNotification> response = apnsClient.sendNotification(notification).get();
        log.info("Sent message to device: " + request.getDeviceToken() + ", " + response.toString());
    }

}