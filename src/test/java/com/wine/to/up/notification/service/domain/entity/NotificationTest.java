package com.wine.to.up.notification.service.domain.entity;

import com.wine.to.up.notification.service.components.NotificationServiceMetricsCollector;
import com.wine.to.up.notification.service.domain.util.NotificationType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class NotificationTest {

    @MockBean
    private NotificationServiceMetricsCollector notificationServiceMetricsCollector;

    @Test
    public void noArgsConstructorTest() {
        Notification notification = new Notification();
        assertThat(notification.getId()).isInstanceOf(Long.class);
    }

    @Test
    public void setIdTest() {
        Long id = 1L;
        Notification notification = new Notification();
        notification.setId(id);
        assertThat(notification.getId()).isEqualTo(id);
    }

    @Test
    public void setMessageTest() {
        String message = "testMessage";
        Notification notification = new Notification();
        notification.setMessage(message);
        assertThat(notification.getMessage()).isEqualTo(message);
    }

    @Test
    public void setTypeTest() {
        NotificationType notificationType = NotificationType.WINE_PRICE_UPDATED;
        Notification notification = new Notification();
        notification.setType(notificationType);
        assertThat(notification.getType()).isEqualTo(notificationType);
    }

    @Test
    public void setUserIdTest() {
        Long userId = 1L;
        Notification notification = new Notification();
        notification.setUserId(userId);
        assertThat(notification.getUserId()).isEqualTo(userId);
    }
}
