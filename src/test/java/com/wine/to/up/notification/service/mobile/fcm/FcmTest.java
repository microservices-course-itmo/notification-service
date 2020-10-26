package com.wine.to.up.notification.service.mobile.fcm;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.wine.to.up.notification.service.domain.model.fcm.FcmPushNotificationRequest;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class FcmTest {
    private FcmService fcmService = new FcmService();

    @Test
    public void testSendMessage() throws ExecutionException, InterruptedException {

        FirebaseOptions options = FirebaseOptions.builder()
                .setProjectId("project-id-1")
                .setCredentials(new MockGoogleCredentials())
                .build();
        FirebaseApp.initializeApp(options);

        FcmPushNotificationRequest fcmPushNotificationRequest = new FcmPushNotificationRequest();
        fcmPushNotificationRequest.setTitle("sample");
        fcmPushNotificationRequest.setMessage("sample");
        fcmPushNotificationRequest.setClientToken("sample");

        Throwable thrown = catchThrowable(() -> {
            fcmService.sendMessage(fcmPushNotificationRequest);
        });

        assertThat(thrown).isInstanceOf(ExecutionException.class)
                .hasMessageContaining("Unexpected HTTP response with status: 401");
    }

    private static class MockGoogleCredentials extends GoogleCredentials {

        @Override
        public AccessToken refreshAccessToken() {
            Date expiry = new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1));
            return new AccessToken(UUID.randomUUID().toString(), expiry);
        }
    }
}
