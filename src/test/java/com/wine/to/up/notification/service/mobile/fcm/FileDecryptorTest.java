package com.wine.to.up.notification.service.mobile.fcm;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.wine.to.up.notification.service.mobile.apns.ApnsService;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class FileDecryptorTest {
    private String firebaseConfigPath;
    private String secretKey;

    private ListAppender<ILoggingEvent> appender;
    private Logger appLogger = (Logger) LoggerFactory.getLogger(ApnsService.class);

    @Before
    public void setUp() {
        appender = new ListAppender<>();
        appender.start();
        appLogger.addAppender(appender);
        firebaseConfigPath = "firebase-config.json.encrypted";
        secretKey = "lOGUwpACjC";
    }

    @Test
    public void decryptFileTest() throws GeneralSecurityException, IOException {
        File decryptedFile = FileDecryptor.decryptFile(firebaseConfigPath, secretKey);
        assertThat(decryptedFile).isNotNull();
    }
}
