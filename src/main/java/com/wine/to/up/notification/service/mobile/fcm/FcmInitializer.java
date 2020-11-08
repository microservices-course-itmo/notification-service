package com.wine.to.up.notification.service.mobile.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;


@Service
@Slf4j
/**
 * Initializes FirebaseApp with creds from file specified in application.properties
 * through the `app.firebase-configuration-file` setting.
 * FirebaseApp is later used for sending pushes to Android in FcmService.
 * @see com.wine.to.up.notification.service.mobile.fcm.FcmService
 */
public class FcmInitializer {
    @Value("${app.firebase-configuration-file}")
    private String firebaseConfigPath;

    @Value("${app.firebase-db-url}")
    private String firebaseDbUrl;

    @PostConstruct
    public void initialize() {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials
                            .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())

                    ).setDatabaseUrl(firebaseDbUrl)
                .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Successfully initialized Firebase app!");
            }
            else {
                log.warn("Attempted to re-initialize an existing Firebase app! Double injection?");
            }
        } catch (IOException e) {
            log.error("Error opening service account file!", e);
        }
    }
}
