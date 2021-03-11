package com.wine.to.up.notification.service.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import java.util.List;
import com.wine.to.up.notification.service.components.NotificationServiceMetricsCollector;
import com.wine.to.up.notification.service.domain.entity.Notification;
import com.wine.to.up.notification.service.dto.NotificationDTO;
import com.wine.to.up.notification.service.domain.util.NotificationType;
import com.wine.to.up.notification.service.exceptions.NotificationNotFoundException;
import com.wine.to.up.notification.service.repository.NotificationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;


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

}