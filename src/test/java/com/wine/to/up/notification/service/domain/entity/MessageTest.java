package com.wine.to.up.notification.service.domain.entity;

import com.wine.to.up.notification.service.components.NotificationServiceMetricsCollector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class MessageTest {

    @MockBean
    private NotificationServiceMetricsCollector notificationServiceMetricsCollector;

    @Test
    public void noArgsConstructorTest() {
        Message message = new Message();
        assertThat(message.getId().getClass()).isEqualTo(UUID.class);
    }

    @Test
    public void ConstructorTest() {
        String content = "testContent";
        Message message = new Message(content);
        assertThat(message.getContent()).isEqualTo(content);
    }

    @Test
    public void setContentTest() {
        String content = "testContent";
        Message message = new Message();
        message.setContent(content);
        assertThat(message.getContent()).isEqualTo(content);
    }

    @Test
    public void setIdTest() {
        UUID id = UUID.randomUUID();
        Message message = new Message();
        message.setId(id);
        assertThat(message.getId()).isEqualTo(id);
    }
}
