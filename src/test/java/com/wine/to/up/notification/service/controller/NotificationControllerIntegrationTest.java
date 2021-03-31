package com.wine.to.up.notification.service.controller;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.wine.to.up.notification.service.components.NotificationServiceMetricsCollector;
import com.wine.to.up.notification.service.domain.entity.Notification;
import com.wine.to.up.notification.service.domain.model.apns.ApnsPushNotificationRequest;
import com.wine.to.up.notification.service.domain.util.NotificationType;
import com.wine.to.up.notification.service.dto.NotificationDTO;
import com.wine.to.up.notification.service.dto.WinePriceUpdatedWithTokensEventDTO;
import com.wine.to.up.notification.service.exceptions.NotificationNotFoundException;
import com.wine.to.up.notification.service.repository.NotificationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class NotificationControllerIntegrationTest {

    @MockBean
    private NotificationServiceMetricsCollector notificationServiceMetricsCollector;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationController notificationController;

    @Before
    public void initializeTestFcm() {
        FirebaseOptions options = FirebaseOptions.builder().setProjectId("project-id-1")
                .setCredentials(new MockGoogleCredentials()).build();
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }

    @Test
    public void testGetNotificationById() {
        Notification notification = new Notification();
        notification.setMessage("testGetById");
        notification.setType(NotificationType.WINE_PRICE_UPDATED);
        notification.setUserId(5);

        Notification created = notificationRepository.save(notification);

        Notification found = notificationController.getNotificationById(created.getId());
        assertThat(found.getMessage()).isEqualTo("testGetById");
    }

    @Test
    public void testPutNotification() {
        NotificationDTO notification = new NotificationDTO();
        notification.setMessage("testPut");
        notification.setType(NotificationType.WINE_PRICE_UPDATED);
        notification.setUserId(5);

        Notification created = notificationController.createNotification(notification);

        Notification found = notificationController.getNotificationById(created.getId());
        assertThat(found.getMessage()).isEqualTo("testPut");
    }

    @Test
    public void testGetNotificationByUserId() {
        NotificationDTO notification = new NotificationDTO();
        notification.setMessage("testGetByUserId");
        notification.setType(NotificationType.WINE_PRICE_UPDATED);
        notification.setUserId(6);

        notificationController.createNotification(notification);

        List<Notification> found = notificationController.getNotificationByUserId(6L);
        assertThat(found.get(0).getMessage()).isEqualTo("testGetByUserId");
    }

    @Test
    public void testUpdateNotification() {
        NotificationDTO notification = new NotificationDTO();
        notification.setMessage("foo");
        notification.setType(NotificationType.WINE_PRICE_UPDATED);
        notification.setUserId(5);

        Notification created = notificationController.createNotification(notification);
        notification.setMessage("bar");
        Notification updated = notificationController.updateNotification(notification, created.getId());

        assertThat(created.getId()).isEqualTo(updated.getId());
        Notification found = notificationController.getNotificationById(updated.getId());
        assertThat(found.getMessage()).isEqualTo("bar");
    }

    @Test
    public void testDeleteNotificationById() {
        NotificationDTO notification = new NotificationDTO();
        notification.setMessage("foo");
        notification.setType(NotificationType.WINE_PRICE_UPDATED);
        notification.setUserId(5);

        Notification created = notificationController.createNotification(notification);

        Notification found = notificationController.getNotificationById(created.getId());
        assertThat(found.getMessage()).isEqualTo("foo");

        notificationController.deleteNotificationById(created.getId());

        long notificationId = created.getId();
        assertThrows(NotificationNotFoundException.class, () -> {
            notificationController.getNotificationById(notificationId);
        });
    }

    @Test
    public void sendMessage() throws Exception {
        ApnsPushNotificationRequest apnsPushNotificationRequest = new ApnsPushNotificationRequest();
        apnsPushNotificationRequest.setDeviceToken("sampleToken");
        apnsPushNotificationRequest.setPayload("samplePayload");
        notificationController.sendIosNotification(apnsPushNotificationRequest);
    }

    @Test
    public void testTriggerKafkaConsumer() throws Exception {
        WinePriceUpdatedWithTokensEventDTO eventDTO = new WinePriceUpdatedWithTokensEventDTO();
        eventDTO.setUserId(456789L);
        eventDTO.setWineName("sampleWineName");
        eventDTO.setWineId("987789");
        eventDTO.setApnsToken("apnsSample");
        eventDTO.setFcmToken("fcmSample");
        eventDTO.setExpoToken("expoSample");
        notificationController.triggerKafkaConsumer(eventDTO);
        List<Notification> notificationList = notificationController.getNotificationByUserId(456789L);
        assertThat(notificationList.size() > 0).isTrue();
    }

    private static class MockGoogleCredentials extends GoogleCredentials {

        @Override
        public AccessToken refreshAccessToken() {
            Date expiry = new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1));
            return new AccessToken(UUID.randomUUID().toString(), expiry);
        }
    }

    @Test
    public void testMarkNotificationViewed() {
        NotificationDTO notification = new NotificationDTO();

        Notification created = notificationController.createNotification(notification);
        assertThat(created.isViewed()).isFalse();

        notificationController.markNotificationViewed(created.getId());

        Notification found = notificationController.getNotificationById(created.getId());
        assertThat(found.isViewed()).isTrue();
    }

}