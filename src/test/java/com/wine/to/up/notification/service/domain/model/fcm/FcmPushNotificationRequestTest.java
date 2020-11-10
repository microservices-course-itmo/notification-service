package com.wine.to.up.notification.service.domain.model.fcm;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class FcmPushNotificationRequestTest {

    @Test
    public void testNoArgsConstructor() {
        FcmPushNotificationRequest FcmPushNotificationRequest = new FcmPushNotificationRequest();
        assertThat(FcmPushNotificationRequest.getTitle()).isNull();
    }

    @Test
    public void testAllArgsConstructor() {
        String title = "testTitle";
        String message = "testTopic";
        String clientToken = "testPayload";
        FcmPushNotificationRequest FcmPushNotificationRequest = new FcmPushNotificationRequest(title, message, clientToken);
        assertThat(FcmPushNotificationRequest.getTitle()).isEqualTo(title);
        assertThat(FcmPushNotificationRequest.getMessage()).isEqualTo(message);
        assertThat(FcmPushNotificationRequest.getClientToken()).isEqualTo(clientToken);
    }

    @Test
    public void testTitleSetter() {
        FcmPushNotificationRequest FcmPushNotificationRequest = new FcmPushNotificationRequest();
        String title = "testTitle";
        FcmPushNotificationRequest.setTitle(title);
        assertThat(FcmPushNotificationRequest.getTitle()).isEqualTo(title);
    }

    @Test
    public void testMessageSetter() {
        FcmPushNotificationRequest FcmPushNotificationRequest = new FcmPushNotificationRequest();
        String message = "testMessage";
        FcmPushNotificationRequest.setMessage(message);
        assertThat(FcmPushNotificationRequest.getMessage()).isEqualTo(message);
    }

    @Test
    public void testClientTokenSetter() {
        FcmPushNotificationRequest FcmPushNotificationRequest = new FcmPushNotificationRequest();
        String clientToken = "testClientToken";
        FcmPushNotificationRequest.setClientToken(clientToken);
        assertThat(FcmPushNotificationRequest.getClientToken()).isEqualTo(clientToken);
    }
}