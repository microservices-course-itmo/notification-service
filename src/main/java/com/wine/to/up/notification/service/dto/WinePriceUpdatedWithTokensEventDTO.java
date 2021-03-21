package com.wine.to.up.notification.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WinePriceUpdatedWithTokensEventDTO {
    private String wineId;
    private String wineName;

    private float newWinePrice;

    private long userId;

    private String fcmToken;
    private String apnsToken;
    private String expoToken;
}
