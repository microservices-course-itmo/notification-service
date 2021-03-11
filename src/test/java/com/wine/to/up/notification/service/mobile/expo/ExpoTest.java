package com.wine.to.up.notification.service.mobile.expo;

import com.wine.to.up.notification.service.domain.model.expo.ExpoNotificationRequest;
import com.wine.to.up.user.service.api.message.UserTokensOuterClass;
import com.wine.to.up.user.service.api.message.WinePriceUpdatedWithTokensEventOuterClass;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ExpoTest {

    private ExpoService service;

    @Before
    public void setUp() {
        this.service = new ExpoService();
    }

    @Test
    public void testSendMessage() throws AssertionError {
        ExpoNotificationRequest request = new ExpoNotificationRequest();
        request.setMessage("test");
        request.setTitle("test");
        request.setToken("test");
        try {
            this.service.sendMessage(request);
        } catch (Exception e) {
            throw new AssertionError("[ExpoService] Failed to send message");
        }
    }

    @Test
    public void testSendAll() {
        List<UserTokensOuterClass.UserTokens> tokens = new ArrayList<>();
        tokens.add(
                UserTokensOuterClass.UserTokens.newBuilder()
                        .addExpoTokens("123")
                        .addExpoTokens("456")
                        .setUserId(1L)
                        .build()
        );

        WinePriceUpdatedWithTokensEventOuterClass.WinePriceUpdatedWithTokensEvent event = WinePriceUpdatedWithTokensEventOuterClass.WinePriceUpdatedWithTokensEvent.newBuilder()
                .setWineId("1")
                .setWineName("test")
                .setNewWinePrice((float)1)
                .addAllUserTokens(tokens)
                .build();

        this.service.sendAll(event); // Assure the error is not thrown
    }
}
