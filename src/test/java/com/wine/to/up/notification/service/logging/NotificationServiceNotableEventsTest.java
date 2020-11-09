package com.wine.to.up.notification.service.logging;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class NotificationServiceNotableEventsTest {

    @Test
    public void D_KAFKA_SEND_MESSAGE_SUCCESS_Test() {
        NotificationServiceNotableEvents notificationServiceNotableEvents = NotificationServiceNotableEvents.D_KAFKA_SEND_MESSAGE_SUCCESS;
        assertThat(notificationServiceNotableEvents.getTemplate()).isEqualTo(NotificationServiceNotableEvents.D_KAFKA_SEND_MESSAGE_SUCCESS.getTemplate());
    }

    @Test
    public void D_CONTROLLER_RECEIVED_MESSAGE_Test() {
        NotificationServiceNotableEvents notificationServiceNotableEvents = NotificationServiceNotableEvents.D_CONTROLLER_RECEIVED_MESSAGE;
        assertThat(notificationServiceNotableEvents.getTemplate()).isEqualTo(NotificationServiceNotableEvents.D_CONTROLLER_RECEIVED_MESSAGE.getTemplate());
    }

    @Test
    public void getNameTest() {
        NotificationServiceNotableEvents notificationServiceNotableEvents = NotificationServiceNotableEvents.D_KAFKA_SEND_MESSAGE_SUCCESS;
        assertThat(notificationServiceNotableEvents.getName()).isEqualTo("D_KAFKA_SEND_MESSAGE_SUCCESS");
    }
}
