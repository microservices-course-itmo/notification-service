package com.wine.to.up.notification.service.components;

import com.wine.to.up.commonlib.metrics.CommonMetricsCollector;
import io.micrometer.core.instrument.Metrics;
import org.springframework.stereotype.Component;

/**
 * This Class expose methods for recording specific metrics
 * It changes metrics of Micrometer and Prometheus simultaneously
 * Micrometer's metrics exposed at /actuator/prometheus
 * Prometheus' metrics exposed at /metrics-prometheus
 *
 */
@Component
public class NotificationServiceMetricsCollector extends CommonMetricsCollector {
    private static final String SERVICE_NAME = "notification_service";

    private static final String MESSAGES_RECEIVED = "messages_received";
    private static final String NOTIFICATIONS_SENT = "notifications_sent";
    private static final String NOTIFICATIONS_FAILED = "notifications_failed";

    private static final String DEVICE_SYSTEM = "device_system";


    public void notificationsFailedInc(String deviceSystem) {
        Metrics.counter(NOTIFICATIONS_FAILED, DEVICE_SYSTEM, deviceSystem).increment();
    }

    public void notificationsSentInc(String deviceSystem) {
        Metrics.counter(NOTIFICATIONS_SENT, DEVICE_SYSTEM, deviceSystem).increment();
    }

    public void messagesReceivedInc() {
        Metrics.counter(MESSAGES_RECEIVED).increment();
    }

    public NotificationServiceMetricsCollector() {
        this(SERVICE_NAME);
    }

    public NotificationServiceMetricsCollector(String serviceName) {
        super(serviceName);
    }
}
