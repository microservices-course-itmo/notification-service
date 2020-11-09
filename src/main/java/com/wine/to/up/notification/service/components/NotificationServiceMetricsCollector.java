package com.wine.to.up.notification.service.components;

import com.wine.to.up.commonlib.metrics.CommonMetricsCollector;
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

    public NotificationServiceMetricsCollector() {
        this(SERVICE_NAME);
    }

    public NotificationServiceMetricsCollector(String serviceName) {
        super(serviceName);
    }
}
