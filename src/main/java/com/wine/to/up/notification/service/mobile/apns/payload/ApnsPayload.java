package com.wine.to.up.notification.service.mobile.apns.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApnsPayload {
    ApnsPayloadAps aps;
    String type;
    String winePositionId;
}
