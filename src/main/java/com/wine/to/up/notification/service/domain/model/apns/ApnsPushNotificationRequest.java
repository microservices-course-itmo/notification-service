package com.wine.to.up.notification.service.domain.model.apns;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/**
 * A data class containing information for sending APNS push notifications.
 */
public class ApnsPushNotificationRequest {
    private String deviceToken;
    private String topic;
    private String payload;
}