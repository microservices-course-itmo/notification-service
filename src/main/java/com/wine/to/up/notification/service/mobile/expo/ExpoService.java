package com.wine.to.up.notification.service.mobile.expo;

import com.kinoroy.expo.push.ExpoPushClient;
import com.kinoroy.expo.push.Message;
import com.wine.to.up.notification.service.domain.model.expo.ExpoNotificationRequest;
import com.wine.to.up.notification.service.mobile.NotificationSender;
import com.wine.to.up.user.service.api.message.WinePriceUpdatedWithTokensEventOuterClass.WinePriceUpdatedWithTokensEvent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class ExpoService implements NotificationSender<ExpoNotificationRequest> {

    @SneakyThrows
    @Override
    public void sendMessage(ExpoNotificationRequest request) throws InterruptedException, ExecutionException {
        try{
            final Message message = new Message.Builder()
                .to(request.getToken())
                .title(request.getTitle())
                .body(request.getMessage())
                .build();

            ExpoPushClient.sendPushNotifications(List.of(message)).getTickets();
        }
        catch (Exception e){
            log.error("Couldn't send Expo push:", e);
        }
    }

    @Override
    public void sendAll(WinePriceUpdatedWithTokensEvent event) {
        final String title = String.format("New discount on %s!", event.getWineName());
        final String message = String.format("New price is: %.1f", event.getNewWinePrice());

        event.getUserTokensList().forEach(userTokens -> userTokens.getExpoTokensList().forEach(token -> {
                    try {
                        sendMessage(new ExpoNotificationRequest(token, title, message));
                    } catch (InterruptedException | ExecutionException e) {
                        log.error("Couldn't send Expo push:", e);
                        Thread.currentThread().interrupt();
                    }
                }
        ));
    }
}
