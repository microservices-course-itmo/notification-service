package com.wine.to.up.notification.service.controller;

import static org.assertj.core.api.Assertions.assertThat;
import com.wine.to.up.notification.service.components.NotificationServiceMetricsCollector;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NotificationControllerSmokeTest {

    @MockBean
    private NotificationServiceMetricsCollector notificationServiceMetricsCollector;

    @Autowired
    private NotificationController controller;

    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }
}