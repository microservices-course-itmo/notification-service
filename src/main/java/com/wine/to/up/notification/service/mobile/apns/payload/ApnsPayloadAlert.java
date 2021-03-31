package com.wine.to.up.notification.service.mobile.apns.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApnsPayloadAlert {
    String title;
    String body;
}
