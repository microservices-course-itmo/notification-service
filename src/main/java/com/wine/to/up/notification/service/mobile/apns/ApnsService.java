package com.wine.to.up.notification.service.mobile.apns;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.ApnsClientBuilder;
import com.eatthepath.pushy.apns.PushNotificationResponse;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.wine.to.up.notification.service.domain.model.apns.ApnsPushNotificationRequest;
import com.wine.to.up.notification.service.mobile.NotificationSender;
import com.wine.to.up.user.service.api.message.WinePriceUpdatedWithTokensEventOuterClass.WinePriceUpdatedWithTokensEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
/**
 * A service used to send push notifications to Apple devices
 * through Apple Push Notification Service (APNS).
 * 
 * Needs @Service anno once we get APNS credentials.
 * 
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
     * @param settings ApnsSettings created on startup
     */
    public ApnsService(ApnsSettings settings) {
        log.info("Creating ApnsService...");
        try {
            final File keyFile = new File(this.getClass().getResource(settings.getFilePath()).toURI());

            ApnsClientBuilder apnsClientBuilder = new ApnsClientBuilder()
                    .setApnsServer(settings.getApnsServerHost(), settings.getApnsServerPort())
                    .setClientCredentials(keyFile, settings.getFilePassword());
            if (settings.getTrustedCertificatePath() != null) {
                apnsClientBuilder.setTrustedServerCertificateChain(
                        this.getClass().getResourceAsStream(settings.getTrustedCertificatePath())
                );
            }
            apnsClient = apnsClientBuilder.build();

            log.info("Successfully initialized APNS client");
        } catch (URISyntaxException | IOException | NullPointerException | IllegalArgumentException e) {
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
        log.info("Sending notification to device: {}", request.getDeviceToken());
        SimpleApnsPushNotification notification = new SimpleApnsPushNotification(
                request.getDeviceToken(), request.getTopic(), request.getPayload()
        );
        PushNotificationResponse<SimpleApnsPushNotification> response = apnsClient.sendNotification(notification).get();
        log.info("Sent message to device: {}, {}", request.getDeviceToken(), response.toString());
    }

    @Override
    public void sendAll(WinePriceUpdatedWithTokensEvent event) {

    }

}
