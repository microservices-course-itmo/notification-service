package com.wine.to.up.notification.service.domain.model.fcm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

/**
 * A data class containing information for sending FCM push notifications.
 */
public class FcmPushNotificationRequest {
    private String title;
    private String message;
    private String clientToken;
}
