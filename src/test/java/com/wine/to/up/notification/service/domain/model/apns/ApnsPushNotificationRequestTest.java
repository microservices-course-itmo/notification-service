package com.wine.to.up.notification.service.domain.model.apns;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class ApnsPushNotificationRequestTest {

    @Test
    public void testNoArgsConstructor() {
        ApnsPushNotificationRequest apnsPushNotificationRequest = new ApnsPushNotificationRequest();
        assertThat(apnsPushNotificationRequest.getDeviceToken()).isEqualTo(null);
    }

    @Test
    public void testAllArgsConstructor() {
        String deviceToken = "testDeviceToken";
        String topic = "testTopic";
        String payload = "testPayload";
        ApnsPushNotificationRequest apnsPushNotificationRequest = new ApnsPushNotificationRequest(deviceToken, topic, payload);
        assertThat(apnsPushNotificationRequest.getDeviceToken()).isEqualTo(deviceToken);
        assertThat(apnsPushNotificationRequest.getTopic()).isEqualTo(topic);
        assertThat(apnsPushNotificationRequest.getPayload()).isEqualTo(payload);
    }

    @Test
    public void testDeviceTokenSetter() {
        ApnsPushNotificationRequest apnsPushNotificationRequest = new ApnsPushNotificationRequest();
        String deviceToken = "testDeviceToken";
        apnsPushNotificationRequest.setDeviceToken(deviceToken);
        assertThat(apnsPushNotificationRequest.getDeviceToken()).isEqualTo(deviceToken);
    }

    @Test
    public void testTopicSetter() {
        ApnsPushNotificationRequest apnsPushNotificationRequest = new ApnsPushNotificationRequest();
        String topic = "testTopic";
        apnsPushNotificationRequest.setTopic(topic);
        assertThat(apnsPushNotificationRequest.getTopic()).isEqualTo(topic);
    }

    @Test
    public void testPayloadSetter() {
        ApnsPushNotificationRequest apnsPushNotificationRequest = new ApnsPushNotificationRequest();
        String payload = "testPayload";
        apnsPushNotificationRequest.setPayload(payload);
        assertThat(apnsPushNotificationRequest.getPayload()).isEqualTo(payload);
    }
}
