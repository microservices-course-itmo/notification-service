package com.wine.to.up.notification.service.mobile.apns;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


//@ConfigurationProperties(prefix = "apns")
//@Component
@Getter
@Setter
public class ApnsSettings {
    private String keyFile;
    private String teamId;
    private String keyId;
}
