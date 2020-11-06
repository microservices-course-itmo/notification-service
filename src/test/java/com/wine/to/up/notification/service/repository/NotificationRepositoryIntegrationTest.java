package com.wine.to.up.notification.service.repository;

import com.wine.to.up.notification.service.domain.entity.Notification;
import com.wine.to.up.notification.service.domain.util.NotificationType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class NotificationRepositoryIntegrationTest {
    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    public void testGetNotificationById() {
        Notification notification = new Notification();
        notification.setId(1);
        notification.setMessage("testGetById");
        notification.setType(NotificationType.UI);
        notification.setUserId(5);

        notificationRepository.save(notification);

        Optional<Notification> found = notificationRepository.findById(1L);
        assertThat(found).isPresent();
        assertThat(found.get().getMessage()).isEqualTo("testGetById");
    }

    @Test
    public void testPutNotification() {
        Notification notification = new Notification();
        notification.setId(2);
        notification.setMessage("testPut");
        notification.setType(NotificationType.UI);
        notification.setUserId(5);

        notificationRepository.save(notification);

        Optional<Notification> found = notificationRepository.findById(2L);
        assertThat(found).isPresent();
        assertThat(found.get().getMessage()).isEqualTo("testPut");
    }

    @Test
    public void testGetNotificationByUserId() {
        Notification notification = new Notification();
        notification.setId(3);
        notification.setMessage("testGetByUserId");
        notification.setType(NotificationType.UI);
        notification.setUserId(6);

        notificationRepository.save(notification);

        List<Notification> found = notificationRepository.findAllByUserId(6L);
        assertThat(found.get(0).getMessage()).isEqualTo("testGetByUserId");
    }

    @Test
    public void testUpdateNotification() {
        Notification notification = new Notification();
        notification.setId(4);
        notification.setMessage("foo");
        notification.setType(NotificationType.UI);
        notification.setUserId(5);

        notificationRepository.save(notification);
        notification.setMessage("bar");
        notificationRepository.save(notification);

        Optional<Notification> found = notificationRepository.findById(4L);
        assertThat(found).isPresent();
        assertThat(found.get().getMessage()).isEqualTo("bar");
    }

    @Test
    public void testDeleteNotification() {
        Notification notification = new Notification();
        notification.setId(5);
        notification.setMessage("foo");
        notification.setType(NotificationType.UI);
        notification.setUserId(5);

        notificationRepository.save(notification);

        Optional<Notification> found = notificationRepository.findById(5L);
        assertThat(found).isPresent();
        assertThat(found.get().getMessage()).isEqualTo("foo");

        notificationRepository.delete(notification);

        Optional<Notification> notFound = notificationRepository.findById(5L);
        assertThat(notFound).isNotPresent();
    }

    @Test
    public void testDeleteNotificationById() {
        Notification notification = new Notification();
        notification.setId(6);
        notification.setMessage("foo");
        notification.setType(NotificationType.UI);
        notification.setUserId(5);

        notificationRepository.save(notification);

        Optional<Notification> found = notificationRepository.findById(6L);
        assertThat(found).isPresent();
        assertThat(found.get().getMessage()).isEqualTo("foo");

        notificationRepository.deleteById(6L);

        Optional<Notification> notFound = notificationRepository.findById(6L);
        assertThat(notFound).isNotPresent();
    }

}
