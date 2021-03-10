package com.wine.to.up.notification.service.mobile.expo;

import com.kinoroy.expo.push.ExpoPushClient;
import com.kinoroy.expo.push.Message;
import com.wine.to.up.notification.service.domain.model.expo.ExpoNotificationRequest;
import com.wine.to.up.notification.service.mobile.NotificationSender;
import com.wine.to.up.user.service.api.message.WinePriceUpdatedWithTokensEventOuterClass.WinePriceUpdatedWithTokensEvent;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class ExpoService implements NotificationSender<ExpoNotificationRequest> {

    @SneakyThrows
    @Override
    public void sendMessage(ExpoNotificationRequest request) throws InterruptedException, ExecutionException {
        try{
            final Message message=new Message.Builder().to(request.getToken()).title(request.getTitle()).body(request.getMessage()).build();
            ExpoPushClient.sendPushNotifications(List.of(message)).getTickets();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void sendAll(WinePriceUpdatedWithTokensEvent event) {
        final String title=event.getWineName();
        final String message=Float.toString(event.getNewWinePrice());
        event.getUserTokensList().forEach(userTokens -> userTokens.getExpoTokensList().forEach(token->{
                    try {
                        sendMessage(new ExpoNotificationRequest(token,title,message));
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                }
        ));
    }
}
