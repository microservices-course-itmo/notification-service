package com.wine.to.up.notification.service.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import com.wine.to.up.notification.service.domain.entity.Notification;
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
        notification.setTypeId(1);
        notification.setUserId(5);

        notificationRepository.save(notification);

        Optional<Notification> found = notificationController.getNotificationById(Long.valueOf(1));
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getMessage()).isEqualTo("testGetById");
    }

    @Test
    public void testPutNotification() {
        Notification notification = new Notification();
        notification.setId(2);
        notification.setMessage("testPut");
        notification.setTypeId(1);
        notification.setUserId(5);

        notificationController.putNotification(notification);

        Optional<Notification> found = notificationController.getNotificationById(Long.valueOf(2));
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getMessage()).isEqualTo("testPut");
    }

    @Test
    public void testGetNotificationByUserId() {
        Notification notification = new Notification();
        notification.setId(3);
        notification.setMessage("testGetByUserId");
        notification.setTypeId(1);
        notification.setUserId(6);

        notificationController.putNotification(notification);

        List<Notification> found = notificationController.getNotificationByUserId(Long.valueOf(6));
        assertThat(found.get(0).getMessage()).isEqualTo("testGetByUserId");
    }

    @Test
    public void testUpdateNotification() {
        Notification notification = new Notification();
        notification.setId(4);
        notification.setMessage("foo");
        notification.setTypeId(1);
        notification.setUserId(5);

        notificationController.putNotification(notification);
        notification.setMessage("bar");
        notificationController.updateNotification(notification);

        Optional<Notification> found = notificationController.getNotificationById(Long.valueOf(4));
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getMessage()).isEqualTo("bar");
    }

    @Test
    public void testDeleteNotification() {
        Notification notification = new Notification();
        notification.setId(5);
        notification.setMessage("foo");
        notification.setTypeId(1);
        notification.setUserId(5);

        notificationController.putNotification(notification);

        Optional<Notification> found = notificationController.getNotificationById(Long.valueOf(5));
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getMessage()).isEqualTo("foo");

        notificationController.deleteNotification(notification);

        Optional<Notification> notFound = notificationController.getNotificationById(Long.valueOf(5));
        assertThat(notFound.isPresent()).isFalse();
    }

}