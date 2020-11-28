package com.wine.to.up.notification.service.messaging;

import com.wine.to.up.notification.service.components.NotificationServiceMetricsCollector;
import com.wine.to.up.user.service.api.dto.WinePriceUpdatedResponse;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore // until we configure kafka on CI
public class KafkaIntegrationTest {
    @Autowired
    private KafkaTemplate<Long, WinePriceUpdatedResponse> kafkaTemplate;

    private boolean testPassed = false;

    @MockBean
    private NotificationServiceMetricsCollector notificationServiceMetricsCollector;

    @Test
    public void test() {
        final WinePriceUpdatedResponse testWine = new WinePriceUpdatedResponse();
        testWine.setWineName("integration test");

        kafkaTemplate.send("wine-response-topic", testWine);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignore) {
        }
        Assert.assertTrue(testPassed);
    }

    @KafkaListener(id = "test-user-service-topic-listener", topics = {"wine-response-topic"},
            containerFactory = "singleFactory")
    public void consumer(WinePriceUpdatedResponse winePriceUpdatedResponse) {
        if (winePriceUpdatedResponse.getWineName().equals("integration test"))
            testPassed = true;
    }
}
