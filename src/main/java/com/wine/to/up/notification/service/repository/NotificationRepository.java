package com.wine.to.up.notification.service.repository;

import com.wine.to.up.notification.service.domain.entity.Notification;
import com.wine.to.up.notification.service.domain.util.NotificationType;
import com.wine.to.up.user.service.api.message.UserTokensOuterClass.UserTokens;
import com.wine.to.up.user.service.api.message.WinePriceUpdatedWithTokensEventOuterClass.WinePriceUpdatedWithTokensEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, Long> {
    List<Notification> findAllByUserIdOrderByTimestampDesc(long userId);

    default List<Notification> saveWinePriceUpdatedWithTokens(WinePriceUpdatedWithTokensEvent event) {
        final String message = String.format("New discount on %s! New price is: %f", event.getWineName(), event.getNewWinePrice());
        final Timestamp ts = new Timestamp(new Date().getTime());

        List<Notification> savedNotifications = new ArrayList<Notification>();
        for (UserTokens tokens : event.getUserTokensList()) {
            Notification notification = Notification.builder()
                .message(message)
                .type(NotificationType.WINE_PRICE_UPDATED)
                .timestamp(ts)
                .userId(tokens.getUserId())
                .wineId(event.getWineId())
                .build();

            notification = save(notification);
            savedNotifications.add(notification);

            LogWrapper.log.debug("Saved Notification with id = {}", notification.getId());
        }

        return savedNotifications;
    }

    @Slf4j
    final class LogWrapper {}
}
