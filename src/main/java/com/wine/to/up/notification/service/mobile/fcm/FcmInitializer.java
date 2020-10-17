package com.wine.to.up.notification.service.mobile.fcm;

import java.io.IOException;
import javax.annotation.PostConstruct;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class FcmInitializer {
    @Value("${app.firebase-configuration-file}")
    private String firebaseConfigPath;

    @PostConstruct
    public void initialize() {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials
                    .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                )
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
