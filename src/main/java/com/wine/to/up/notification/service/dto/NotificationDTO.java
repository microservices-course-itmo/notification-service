package com.wine.to.up.notification.service.dto;

import com.wine.to.up.notification.service.domain.util.NotificationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class NotificationDTO {
    private String message;
    private NotificationType type;
    private long timestamp;
    private long userId;
    private String wineId;
}
