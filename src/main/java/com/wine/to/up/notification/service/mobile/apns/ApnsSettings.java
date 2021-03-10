package com.wine.to.up.notification.service.mobile.apns;

import com.eatthepath.pushy.apns.ApnsClientBuilder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;



@ConfigurationProperties(prefix = "apns")
@Component
@Getter
@Setter
/**
 * A settings class used to load credentials from application.properties
 * to later be used for pushy's ApnsClient initialization in ApnsService.
 * @see com.wine.to.up.notification.service.mobile.apns.ApnsService
 */
public class ApnsSettings {
    private String filePath;
    private String filePassword;

    private String p8FilePath;
    private String teamId;
    private String keyId;
    private String appBundleId;

    private String trustedCertificatePath = null;
    private String apnsServerHost = ApnsClientBuilder.DEVELOPMENT_APNS_HOST;
    private int apnsServerPort = 443;
}
