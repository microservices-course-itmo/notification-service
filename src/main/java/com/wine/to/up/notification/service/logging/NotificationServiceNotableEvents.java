package com.wine.to.up.notification.service.logging;

import com.wine.to.up.commonlib.logging.NotableEvent;

public enum NotificationServiceNotableEvents implements NotableEvent {
    D_KAFKA_SEND_MESSAGE_SUCCESS("Kafka send message: {}"),
    D_CONTROLLER_RECEIVED_MESSAGE("Message: {}");

    private final String template;

    NotificationServiceNotableEvents(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

    @Override
    public String getName() {
        return name();
    }


}
