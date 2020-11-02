package com.wine.to.up.notification.service.mobile.apns;

import com.eatthepath.pushy.apns.server.*;
import com.wine.to.up.notification.service.domain.model.apns.ApnsPushNotificationRequest;
import org.junit.Test;

import java.io.File;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class ApnsTest {
    private static final int PORT = 15555;

    private static final String CA_CERTIFICATE_FILENAME = "/ca.pem";
    private static final String SERVER_CERTIFICATE_FILENAME = "/server-certs.pem";
    private static final String SERVER_KEY_FILENAME = "/server-key.pem";

    private ApnsSettings apnsSettings;
    private ApnsService apnsService;
    private MockApnsServer apnsServer;

    @Test
    public void testSendMessage() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(256, new SecureRandom());

        apnsSettings = new ApnsSettings();
        apnsSettings.setFilePath("/single-topic-client.p12");
        apnsSettings.setFilePassword("pushy-test");
        apnsSettings.setTrustedCertificatePath(CA_CERTIFICATE_FILENAME);
        apnsSettings.setApnsServerHost("localhost");
        apnsSettings.setApnsServerPort(PORT);

        apnsService = new ApnsService(apnsSettings);

        try {
            apnsServer = createApnsServer();
        } catch (Exception e) {
            throw new AssertionError("Failed to create apnsServer", e);
        }
        apnsServer.start(PORT).get();

        ApnsPushNotificationRequest apnsPushNotificationRequest = new ApnsPushNotificationRequest();
        apnsPushNotificationRequest.setDeviceToken("sample");
        apnsPushNotificationRequest.setPayload("sample");
        apnsPushNotificationRequest.setTopic("sample");

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
        public void handlePushNotificationAccepted(io.netty.handler.codec.http2.Http2Headers http2Headers, io.netty.buffer.ByteBuf byteBuf) {
            ApnsPushNotificationStorage.INSTANCE.addNotification(byteBuf);
        }

        @Override
        public void handlePushNotificationRejected(io.netty.handler.codec.http2.Http2Headers http2Headers, io.netty.buffer.ByteBuf byteBuf, RejectionReason rejectionReason, Instant instant) {
        }
    }

}
