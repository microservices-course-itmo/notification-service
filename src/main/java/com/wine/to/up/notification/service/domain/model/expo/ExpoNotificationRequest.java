package com.wine.to.up.notification.service.domain.model.expo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpoNotificationRequest {
    private String token;
    private String title;
    private String message;
    private HashMap<String, Object> data;
}
