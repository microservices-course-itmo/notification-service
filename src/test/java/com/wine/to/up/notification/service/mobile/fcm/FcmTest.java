package com.wine.to.up.notification.service.mobile.fcm;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.wine.to.up.notification.service.domain.model.fcm.FcmPushNotificationRequest;
import com.wine.to.up.user.service.api.message.UserTokensOuterClass.UserTokens;
import com.wine.to.up.user.service.api.message.WinePriceUpdatedWithTokensEventOuterClass.WinePriceUpdatedWithTokensEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class FcmTest {
    @Autowired
    private FcmService fcmService;

    @Before
    public void initializeTestFcm() {
        FirebaseOptions options = FirebaseOptions.builder().setProjectId("project-id-1")
                .setCredentials(new MockGoogleCredentials()).build();
        
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }

    @Test
    public void testSendMessage() {
        FcmPushNotificationRequest fcmPushNotificationRequest = new FcmPushNotificationRequest();
        fcmPushNotificationRequest.setTitle("sample");
        fcmPushNotificationRequest.setMessage("sample");
        fcmPushNotificationRequest.setClientToken("sample");

        Throwable thrown = catchThrowable(() -> {
            fcmService.sendMessage(fcmPushNotificationRequest);
        });

        assertThat(thrown).isInstanceOf(ExecutionException.class).hasMessageContaining("401"); // Because we're using mock creds
    }

    @Test
    public void testSendAll() {
        List<UserTokens> tokens = new ArrayList<>();
        tokens.add(
            UserTokens.newBuilder()
                .addFcmTokens("123")
                .addFcmTokens("456")
                .setUserId(1L)
                .build()
        );

        WinePriceUpdatedWithTokensEvent event = WinePriceUpdatedWithTokensEvent.newBuilder()
            .setWineId("1")
            .setWineName("test")
            .setNewWinePrice((float)1)
            .addAllUserTokens(tokens)
            .build();

        fcmService.sendAll(event);  // Assure the error is not thrown
    }

    /**
     * Recommended way to test Firebase apps. That's how it's actually done in firebase-admin tests.
     */
    private static class MockGoogleCredentials extends GoogleCredentials {

        @Override
        public AccessToken refreshAccessToken() {
            Date expiry = new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1));
            return new AccessToken(UUID.randomUUID().toString(), expiry);
        }
    }
}
