package com.wine.to.up.notification.service.service;

import java.util.concurrent.ExecutionException;

public interface NotificationSender<T> {
    public void sendMessage(T request) throws InterruptedException, ExecutionException;
}
