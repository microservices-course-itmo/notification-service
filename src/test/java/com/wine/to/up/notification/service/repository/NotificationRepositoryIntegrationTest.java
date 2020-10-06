package com.wine.to.up.notification.service.repository;

import com.wine.to.up.notification.service.domain.entity.Notification;
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
        notification.setTypeId(1);
        notification.setUserId(5);

        notificationRepository.save(notification);

        Optional<Notification> found = notificationRepository.findById(Long.valueOf(1));
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

        notificationRepository.save(notification);

        Optional<Notification> found = notificationRepository.findById(Long.valueOf(2));
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

        notificationRepository.save(notification);

        List<Notification> found = notificationRepository.findAllByUserId(Long.valueOf(6));
        assertThat(found.get(0).getMessage()).isEqualTo("testGetByUserId");
    }

    @Test
    public void testUpdateNotification() {
        Notification notification = new Notification();
        notification.setId(4);
        notification.setMessage("foo");
        notification.setTypeId(1);
        notification.setUserId(5);

        notificationRepository.save(notification);
        notification.setMessage("bar");
        notificationRepository.save(notification);

        Optional<Notification> found = notificationRepository.findById(Long.valueOf(4));
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

        notificationRepository.save(notification);

        Optional<Notification> found = notificationRepository.findById(Long.valueOf(5));
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getMessage()).isEqualTo("foo");

        notificationRepository.delete(notification);

        Optional<Notification> notFound = notificationRepository.findById(Long.valueOf(5));
        assertThat(notFound.isPresent()).isFalse();
    }
}
