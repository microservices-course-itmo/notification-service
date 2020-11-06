package com.wine.to.up.notification.service.mobile.apns;

import java.util.ArrayList;

public class ApnsPushNotificationStorage {
    public static final ApnsPushNotificationStorage INSTANCE = new ApnsPushNotificationStorage();
    private ArrayList<io.netty.buffer.ByteBuf> queue = new ArrayList();

    private ApnsPushNotificationStorage() {
    }

    public void addNotification(io.netty.buffer.ByteBuf payload) {
        queue.add(payload);
    }

    public ArrayList<io.netty.buffer.ByteBuf> getQueue() {
        return queue;
    }
}
