package com.wine.to.up.notification.service.dto;

import com.wine.to.up.notification.service.domain.util.NotificationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class NotificationDTO {
    private long id;
    private String message;
    private NotificationType type;
    private LocalDateTime dateTime;
    private long userId;
    private long wineId;
}
