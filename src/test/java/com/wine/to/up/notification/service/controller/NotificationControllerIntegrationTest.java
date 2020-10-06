package com.wine.to.up.notification.service.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import java.util.List;

import com.wine.to.up.notification.service.domain.entity.Notification;
import com.wine.to.up.notification.service.domain.util.NotificationType;
import com.wine.to.up.notification.service.exceptions.NotificationNotFoundException;
import com.wine.to.up.notification.service.repository.NotificationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class NotificationControllerIntegrationTest {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationController notificationController;

    @Test
    public void testGetNotificationById() {
        Notification notification = new Notification();
        notification.setId(1);
        notification.setMessage("testGetById");
        notification.setType(NotificationType.UI);
        notification.setUserId(5);

        notificationRepository.save(notification);

        Notification found = notificationController.getNotificationById(1L);
        assertThat(found.getMessage()).isEqualTo("testGetById");
    }

    @Test
    public void testPutNotification() {
        Notification notification = new Notification();
        notification.setId(2);
        notification.setMessage("testPut");
        notification.setType(NotificationType.UI);
        notification.setUserId(5);

        notificationController.createNotification(notification);

        Notification found = notificationController.getNotificationById(2L);
        assertThat(found.getMessage()).isEqualTo("testPut");
    }

    @Test
    public void testGetNotificationByUserId() {
        Notification notification = new Notification();
        notification.setId(3);
        notification.setMessage("testGetByUserId");
        notification.setType(NotificationType.UI);
        notification.setUserId(6);

        notificationController.createNotification(notification);

        List<Notification> found = notificationController.getNotificationByUserId(6L);
        assertThat(found.get(0).getMessage()).isEqualTo("testGetByUserId");
    }

    @Test
    public void testUpdateNotification() {
        Notification notification = new Notification();
        notification.setId(4);
        notification.setMessage("foo");
        notification.setType(NotificationType.UI);
        notification.setUserId(5);

        notificationController.createNotification(notification);
        notification.setMessage("bar");
        notificationController.updateNotification(notification);

        Notification found = notificationController.getNotificationById(4L);
        assertThat(found.getMessage()).isEqualTo("bar");
    }

    @Test
    public void testDeleteNotification() {
        Notification notification = new Notification();
        notification.setId(5);
        notification.setMessage("foo");
        notification.setType(NotificationType.UI);
        notification.setUserId(5);

        notificationController.createNotification(notification);

        Notification found = notificationController.getNotificationById(5L);
        assertThat(found.getMessage()).isEqualTo("foo");

        notificationController.deleteNotification(notification);

        assertThrows(NotificationNotFoundException.class, () -> {
            notificationController.getNotificationById(5L);
        });
    }

    @Test
    public void testDeleteNotificationById() {
        Notification notification = new Notification();
        notification.setId(6);
        notification.setMessage("foo");
        notification.setType(NotificationType.UI);
        notification.setUserId(5);

        notificationController.createNotification(notification);

        Notification found = notificationController.getNotificationById(6L);
        assertThat(found.getMessage()).isEqualTo("foo");

        notificationController.deleteNotificationById(6L);

        assertThrows(NotificationNotFoundException.class, () -> {
            notificationController.getNotificationById(6L);
        });
    }

}