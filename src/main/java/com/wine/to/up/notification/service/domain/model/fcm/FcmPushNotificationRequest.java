package com.wine.to.up.notification.service.domain.model.fcm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FcmPushNotificationRequest {
    private String title;
    private String topic;
    private String message;
    private String clientToken;
}
