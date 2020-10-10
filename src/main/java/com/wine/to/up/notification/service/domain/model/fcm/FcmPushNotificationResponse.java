package com.wine.to.up.notification.service.domain.model.fcm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FcmPushNotificationResponse {
    private int status;
    private String message;
}
