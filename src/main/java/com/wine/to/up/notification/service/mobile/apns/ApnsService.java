package com.wine.to.up.notification.service.mobile.apns;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.ApnsClientBuilder;
import com.eatthepath.pushy.apns.PushNotificationResponse;
import com.eatthepath.pushy.apns.auth.ApnsSigningKey;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.wine.to.up.notification.service.components.NotificationServiceMetricsCollector;
import com.wine.to.up.notification.service.domain.model.apns.ApnsPushNotificationRequest;
import com.wine.to.up.notification.service.mobile.FileDecryptor;
import com.wine.to.up.notification.service.mobile.NotificationSender;
import com.wine.to.up.user.service.api.message.WinePriceUpdatedWithTokensEventOuterClass.WinePriceUpdatedWithTokensEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URI;
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

    private static final String DEVICE_SYSTEM = "IOs";

    /**
     * Pushy's APNS client for sending Apple pushes.
     */
    private final String topic;
    private ApnsClient apnsClient;

    @Autowired
    private NotificationServiceMetricsCollector metrics;

    /**
     * Reads settings from ApnsSettings, tries to read APNS cert file.
     * If read successfully, initializes ApnsClient.
     * 
     * @param settings ApnsSettings created on startup
     */
    public ApnsService(ApnsSettings settings) {
        log.info("Creating ApnsService...");

        this.topic = settings.getAppBundleId();

        try {
            File decryptedFile = FileDecryptor.decryptFile(settings.getP8FilePath(), settings.getP8DecryptPassword());
            final URI keyFileUri = decryptedFile.toURI();

            ApnsClientBuilder apnsClientBuilder = new ApnsClientBuilder()
                    .setApnsServer(settings.getApnsServerHost(), settings.getApnsServerPort())
                    .setSigningKey(ApnsSigningKey.loadFromPkcs8File(
                            new File(keyFileUri), settings.getTeamId(), settings.getKeyId())
                    );
            if (settings.getTrustedCertificatePath() != null) {
                apnsClientBuilder.setTrustedServerCertificateChain(
                        this.getClass().getResourceAsStream(settings.getTrustedCertificatePath())
                );
            }
            apnsClient = apnsClientBuilder.build();

            log.info("Successfully initialized APNS client");
        } catch (Exception e) {
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
    public void sendMessage(ApnsPushNotificationRequest request) throws ExecutionException, InterruptedException {
        log.info("Sending notification to device: {}", request.getDeviceToken());
        SimpleApnsPushNotification notification = new SimpleApnsPushNotification(
                request.getDeviceToken(), this.topic, request.getPayload()
        );
        PushNotificationResponse<SimpleApnsPushNotification> response = apnsClient.sendNotification(notification).get();
        metrics.notificationsSentInc(DEVICE_SYSTEM);
        log.info("Sent message to device: {}, {}", request.getDeviceToken(), response.toString());
    }

    private String generatePriceUpdatedPayload(String wineName, float winePrice, String wineId) {
        final String body = "New discount on " + wineName + "! New price is: " + winePrice;
        return "{"
                + "\"aps\": {\"alert\": {\"title\": \"Got new discount!\", \"body\": \"" + body + "\"}}, "
                + "\"type\": \"FAVORITE_POSITION_PRICE_DECREASE\", "
                + "\"winePositionId\": \"" + wineId + "\""
                + "}";
    }

    @Override
    public void sendAll(WinePriceUpdatedWithTokensEvent event) {
        final String payload = this.generatePriceUpdatedPayload(
                event.getWineName(), event.getNewWinePrice(), event.getWineId()
        );
        event.getUserTokensList().forEach(t-> t.getIosTokensList().forEach(token-> {
            final ApnsPushNotificationRequest apnsPushNotificationRequest = new ApnsPushNotificationRequest(token, payload);
            try {
                this.sendMessage(apnsPushNotificationRequest);
            } catch (InterruptedException e) {
                log.warn("Failed to send iOS notification! {}", apnsPushNotificationRequest.toString());
                metrics.notificationsFailedInc(DEVICE_SYSTEM);
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                log.warn("Failed to send iOS notification! {}", apnsPushNotificationRequest.toString());
                metrics.notificationsFailedInc(DEVICE_SYSTEM);
            }
        }));
    }

}
