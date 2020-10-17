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

@Service
@Slf4j
/**
 * A service used to send push notifications to Apple devices
 * through Apple Push Notification Service (APNS).
 * Uses ApnsSettings to load credentials from application.properties.
 * @see com.wine.to.up.notification.service.mobile.apns.ApnsSettings
 */
public class ApnsService implements NotificationSender<ApnsPushNotificationRequest> {

    /**
     * Pushy's APNS client for sending Apple pushes.
     */
    private ApnsClient apnsClient;

    /**
     * Reads settings from ApnsSettings, tries to read APNS cert file.
     * If read successfully, initializes ApnsClient.
     * 
     * @param settings  ApnsSettings created on startup
     */
    public ApnsService(ApnsSettings settings) {
        Path p;
        try {
            p = Paths.get(settings.getKeyFile());
        } catch (NullPointerException e) {
            log.error("Error reading APNS certificate file: " + settings.getKeyFile());
            return;
        }

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
    /**
     * Implementation of NotificationSender's sendMessage for APNS.
     * 
     * @param request  Data object with APNS push information
     * 
     * @throws ExecutionException
     * @throws InterruptedException
     * 
     * @see com.wine.to.up.notification.service.domain.model.apns.ApnsPushNotificationRequest
     * @see com.wine.to.up.notification.service.mobile.NotificationSender
     */
    public void sendMessage(ApnsPushNotificationRequest request)
            throws ExecutionException, InterruptedException {
        SimpleApnsPushNotification notification = new SimpleApnsPushNotification(
                request.getDeviceToken(), request.getTopic(), request.getPayload()
        );
        PushNotificationResponse<SimpleApnsPushNotification> response = apnsClient.sendNotification(notification).get();
        log.info("Sent message to device: " + request.getDeviceToken() + ", " + response.toString());
    }

}