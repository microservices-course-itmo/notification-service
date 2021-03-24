package com.wine.to.up.notification.service.mobile.apns;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.eatthepath.pushy.apns.server.*;
import com.wine.to.up.notification.service.domain.model.apns.ApnsPushNotificationRequest;
import ch.qos.logback.classic.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http2.Http2Headers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class ApnsTest {
    private static final int PORT = 15555;

    private static final String CA_CERTIFICATE_FILENAME = "/ca.pem";
    private static final String SERVER_CERTIFICATE_FILENAME = "/server-certs.pem";
    private static final String SERVER_KEY_FILENAME = "/server-key.pem";

    @Autowired
    private ApnsService apnsService;
    private MockApnsServer apnsServer;

    private ListAppender<ILoggingEvent> appender;
    private Logger appLogger = (Logger) LoggerFactory.getLogger(ApnsService.class);

    @Before
    public void setUp() {
        appender = new ListAppender<>();
        appender.start();
        appLogger.addAppender(appender);
    }

    @After
    public void tearDown() {
        appLogger.detachAppender(appender);
    }

    @Test
    public void testSendMessage() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(256, new SecureRandom());

        try {
            apnsServer = createApnsServer();
        } catch (Exception e) {
            throw new AssertionError("Failed to create apnsServer", e);
        }
        apnsServer.start(PORT).get();

        ApnsPushNotificationRequest apnsPushNotificationRequest = new ApnsPushNotificationRequest();
        apnsPushNotificationRequest.setDeviceToken("sample");
        apnsPushNotificationRequest.setPayload("sample");

        try {
            apnsService.sendMessage(apnsPushNotificationRequest);
        } catch (Exception e) {
            throw new AssertionError("Failed to send message to apnsServer", e);
        }

        ArrayList<io.netty.buffer.ByteBuf> queue = ApnsPushNotificationStorage.INSTANCE.getQueue();
        assertThat(queue.size()).isEqualTo(1);

        apnsServer.shutdown();
    }

    private MockApnsServer createApnsServer() throws Exception {
        final File certificateFile = new File(this.getClass().getResource(SERVER_CERTIFICATE_FILENAME).toURI());
        final File keyFile = new File(this.getClass().getResource(SERVER_KEY_FILENAME).toURI());
        PushNotificationListener listener = new PushNotificationListener();

        return new MockApnsServerBuilder()
                .setServerCredentials(certificateFile, keyFile, null)
                .setTrustedClientCertificateChain(getClass().getResourceAsStream(CA_CERTIFICATE_FILENAME))
                .setHandlerFactory(new AcceptAllPushNotificationHandlerFactory())
                .setListener(listener)
                .build();
    }

    private class PushNotificationListener implements MockApnsServerListener {

        @Override
        public void handlePushNotificationAccepted(Http2Headers http2Headers, ByteBuf byteBuf) {
            ApnsPushNotificationStorage.INSTANCE.addNotification(byteBuf);
        }

        @Override
        public void handlePushNotificationRejected(Http2Headers http2Headers, ByteBuf byteBuf, RejectionReason rejectionReason, Instant instant) {
        }
    }

}
