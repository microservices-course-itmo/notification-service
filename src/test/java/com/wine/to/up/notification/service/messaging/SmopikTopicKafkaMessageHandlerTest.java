package com.wine.to.up.notification.service.messaging;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class SmopikTopicKafkaMessageHandlerTest {

    @Test
    public void test() {
        SmopikTopicKafkaMessageHandler smopikTopicKafkaMessageHandler = new SmopikTopicKafkaMessageHandler();
        smopikTopicKafkaMessageHandler.handle("sampleMessage");
        assertThat(smopikTopicKafkaMessageHandler.getCounter().get() > 0).isTrue();
    }
}
