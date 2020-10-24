package com.wine.to.up.notification.service.exceptions;


public class NotificationNotFoundException extends RuntimeException {
    public NotificationNotFoundException(Long id) {
        super("Could not find notification " + id);
    }
}
