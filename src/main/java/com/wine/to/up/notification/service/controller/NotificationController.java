package com.wine.to.up.notification.service.controller;

import com.wine.to.up.notification.service.domain.entity.Notification;
import com.wine.to.up.notification.service.domain.model.apns.ApnsPushNotificationRequest;
import com.wine.to.up.notification.service.dto.NotificationDTO;
import com.wine.to.up.notification.service.dto.WinePriceUpdatedWithTokensEventDTO;
import com.wine.to.up.notification.service.exceptions.NotificationNotFoundException;
import com.wine.to.up.notification.service.messaging.UserServiceKafkaMessageHandler;
import com.wine.to.up.notification.service.mobile.apns.ApnsService;
import com.wine.to.up.notification.service.repository.NotificationRepository;
import com.wine.to.up.user.service.api.message.UserTokensOuterClass.UserTokens;
import com.wine.to.up.user.service.api.message.WinePriceUpdatedWithTokensEventOuterClass.WinePriceUpdatedWithTokensEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/notification")
@Validated
@Slf4j
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ApnsService apnsService;


    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @PutMapping(value = "/")
    public Notification updateNotification(@RequestBody NotificationDTO notification, @PathVariable(value = "id") Long id) {
        log.debug("New notification CRUD request. Action = UPDATE.");
        Notification persistentNotification = Notification.builder()
            .id(id)
            .message(notification.getMessage())
            .type(notification.getType())
            .timestamp(new Timestamp(notification.getTimestamp()))
            .userId(notification.getUserId())
            .wineId(notification.getWineId())
            .build();

        Notification updated = notificationRepository.save(persistentNotification);
        log.debug("Notification entry updated. id = {}", updated.getId());

        return updated;
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public Notification getNotificationById(@PathVariable(value = "id") Long id) {
        log.debug("New notification CRUD request. Action = GET. id = {}", id);
        return notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));
    }

    @GetMapping(value = "/")
    @ResponseBody
    public List<Notification> getNotificationByUserId(@RequestParam(value = "userId") Long id) {
        log.debug("New notification CRUD request. Action = GET. user_id = {}", id);
        return notificationRepository.findAllByUserIdOrderByViewedAscTimestampDesc(id);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteNotificationById(@PathVariable(value = "id") Long id) {
        log.debug("New notification CRUD request. Action = DELETE. id = {}", id);
        notificationRepository.deleteById(id);
        log.info("Notification entry deleted. id = {}", id);
    }

    @PostMapping(value = "/")
    public Notification createNotification(@RequestBody NotificationDTO notification) {
        log.debug("New notification CRUD request. Action = SAVE.");
        Notification persistentNotification = Notification.builder()
            .message(notification.getMessage())
            .type(notification.getType())
            .timestamp(new Timestamp(notification.getTimestamp()))
            .userId(notification.getUserId())
            .wineId(notification.getWineId())
            .build();

        Notification saved = notificationRepository.save(persistentNotification);
        log.debug("Notification entry saved. id = {}", saved.getId());

        return saved;
    }

    @PutMapping(value = "/view/{id}")
    public void markNotificationViewed(@PathVariable(value = "id") Long id) {
        log.debug("New notification CRUD request. Action = MARK VIEWED.");

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));

        notification.setViewed(true);
        notificationRepository.save(notification);

        log.debug("Notification entry marked as viewed. id = {}", notification.getId());
    }

    @PostMapping(value = "/ios")
    public void sendIosNotification(@RequestBody ApnsPushNotificationRequest notificationRequest) {
        log.debug("Sending ios push-notification");
        try {
            apnsService.sendMessage(notificationRequest);
        }
        catch (Exception e) {
            log.error("Failed to send push-notification", e);
        }
    }

    @Autowired
    private UserServiceKafkaMessageHandler userServiceKafkaMessageHandler;

    @PostMapping(value = "/trigger_kafka_consumer")
    public void triggerKafkaConsumer(@RequestBody WinePriceUpdatedWithTokensEventDTO eventDTO) {
        UserTokens tokens = UserTokens.newBuilder()
            .setUserId(eventDTO.getUserId())
            .addFcmTokens(eventDTO.getFcmToken())
            .addIosTokens(eventDTO.getApnsToken())
            .addExpoTokens(eventDTO.getExpoToken())
            .build();

        WinePriceUpdatedWithTokensEvent event = WinePriceUpdatedWithTokensEvent.newBuilder()
            .setWineId(eventDTO.getWineId())
            .setWineName(eventDTO.getWineName())
            .setNewWinePrice(eventDTO.getNewWinePrice())
            .addUserTokens(tokens)
            .build();

        userServiceKafkaMessageHandler.handle(event);
    }
}
