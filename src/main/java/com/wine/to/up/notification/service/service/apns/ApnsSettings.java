package com.wine.to.up.notification.service.service.apns;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@ConfigurationProperties(prefix = "apns")
@Component
@Getter
public class ApnsSettings {
    private String keyFile;
    private String teamId;
    private String keyId;
}
