package com.wine.to.up.notification.service.service.apns;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.ApnsClientBuilder;
import com.eatthepath.pushy.apns.auth.ApnsSigningKey;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.eatthepath.pushy.apns.util.concurrent.PushNotificationFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
public class ApnsNotificationClient {

    private ApnsClient apnsClient;

    public ApnsNotificationClient(ApnsSettings settings) {
        Path p = Paths.get(settings.getServiceAccountFile());
        try (InputStream stream = Files.newInputStream(p)) {
            apnsClient = new ApnsClientBuilder()
                    .setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST)
                    .setSigningKey(ApnsSigningKey.loadFromInputStream(stream, settings.getTeamId(), settings.getKeyId()))
                    .build();
            log.info("Successfully initialized APNS");
        } catch (IOException e) {
            log.error("Error opening service account file", e);
        } catch (NoSuchAlgorithmException e) {
            log.error("No such algorithm exception", e);
        } catch (InvalidKeyException e) {
            log.error("Invalid key exception", e);
        }
    }

    public PushNotificationFuture sendMessage(ApnsPushNotificationRequest request) {
        SimpleApnsPushNotification notification = new SimpleApnsPushNotification(
                request.getDeviceToken(), request.getTopic(), request.getPayload()
        );
        return apnsClient.sendNotification(notification);
    }

}