package com.wine.to.up.notification.service.repository;

import com.wine.to.up.notification.service.components.NotificationServiceMetricsCollector;
import com.wine.to.up.notification.service.domain.entity.Notification;
import com.wine.to.up.notification.service.domain.util.NotificationType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class NotificationRepositoryIntegrationTest {
    @Autowired
    private NotificationRepository notificationRepository;

    @MockBean
    private NotificationServiceMetricsCollector notificationServiceMetricsCollector;

    @Test
    public void testGetNotificationById() {
        Notification notification = new Notification();
        notification.setMessage("testGetById");
        notification.setType(NotificationType.WINE_PRICE_UPDATED);
        notification.setUserId(5);
        notification.setWineId("33");
        notification.setTimestamp(new Timestamp(new Date().getTime()));

        Notification created = notificationRepository.save(notification);

        Optional<Notification> found = notificationRepository.findById(created.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getMessage()).isEqualTo("testGetById");
    }

    @Test
    public void testPutNotification() {
        Notification notification = new Notification();
        notification.setMessage("testPut");
        notification.setType(NotificationType.WINE_PRICE_UPDATED);
        notification.setUserId(5);
        notification.setWineId("33");
        notification.setTimestamp(new Timestamp(new Date().getTime()));

        Notification created = notificationRepository.save(notification);

        Optional<Notification> found = notificationRepository.findById(created.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getMessage()).isEqualTo("testPut");
    }

    @Test
    public void testGetNotificationByUserId() {
        Notification notification = new Notification();
        notification.setMessage("testGetByUserId");
        notification.setType(NotificationType.WINE_PRICE_UPDATED);
        notification.setUserId(6);
        notification.setWineId("33");
        notification.setTimestamp(new Timestamp(new Date().getTime()));

        notificationRepository.save(notification);

        List<Notification> found = notificationRepository.findAllByUserIdOrderByTimestampDesc(6L);
        assertThat(found.get(0).getMessage()).isEqualTo("testGetByUserId");
    }

    @Test
    public void testUpdateNotification() {
        Notification notification = new Notification();
        notification.setMessage("foo");
        notification.setType(NotificationType.WINE_PRICE_UPDATED);
        notification.setUserId(5);
        notification.setWineId("33");
        notification.setTimestamp(new Timestamp(new Date().getTime()));

        Notification created = notificationRepository.save(notification);
        created.setMessage("bar");
        Notification updated = notificationRepository.save(created);

        assertThat(created.getId()).isEqualTo(updated.getId());
        Optional<Notification> found = notificationRepository.findById(created.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getMessage()).isEqualTo("bar");
    }

    @Test
    public void testDeleteNotification() {
        Notification notification = new Notification();
        notification.setMessage("foo");
        notification.setType(NotificationType.WINE_PRICE_UPDATED);
        notification.setUserId(5);
        notification.setWineId("33");
        notification.setTimestamp(new Timestamp(new Date().getTime()));

        Notification created = notificationRepository.save(notification);

        Optional<Notification> found = notificationRepository.findById(created.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getMessage()).isEqualTo("foo");

        notificationRepository.delete(notification);

        Optional<Notification> notFound = notificationRepository.findById(created.getId());
        assertThat(notFound).isNotPresent();
    }

    @Test
    public void testDeleteNotificationById() {
        Notification notification = new Notification();
        notification.setMessage("foo");
        notification.setType(NotificationType.WINE_PRICE_UPDATED);
        notification.setUserId(5);
        notification.setWineId("33");
        notification.setTimestamp(new Timestamp(new Date().getTime()));

        Notification created = notificationRepository.save(notification);

        Optional<Notification> found = notificationRepository.findById(created.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getMessage()).isEqualTo("foo");

        notificationRepository.deleteById(created.getId());

        Optional<Notification> notFound = notificationRepository.findById(created.getId());
        assertThat(notFound).isNotPresent();
    }

}
