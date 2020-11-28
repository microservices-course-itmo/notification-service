package com.wine.to.up.notification.service.controller;

import com.wine.to.up.notification.service.domain.entity.Notification;
import com.wine.to.up.notification.service.dto.NotificationDTO;
import com.wine.to.up.notification.service.exceptions.NotificationNotFoundException;
import com.wine.to.up.notification.service.messaging.UserServiceKafkaMessageHandler;
import com.wine.to.up.notification.service.repository.NotificationRepository;
import com.wine.to.up.user.service.api.dto.WinePriceUpdatedResponse;
import com.wine.to.up.user.service.api.message.WinePriceUpdatedWithTokensEventOuterClass.WinePriceUpdatedWithTokensEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@Validated
@Slf4j
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @PutMapping(value = "/")
    public void updateNotification(@RequestBody NotificationDTO notification) {
        log.debug("New notification CRUD request. Action = UPDATE. id = {}",notification.getId());
        Notification persistentNotification = new Notification();
        persistentNotification.setId(notification.getId());
        persistentNotification.setMessage(notification.getMessage());
        persistentNotification.setType(notification.getType());
        persistentNotification.setUserId(notification.getUserId());
        log.debug("Notification entry updated. id = {}",persistentNotification.getId());
        notificationRepository.save(persistentNotification);
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public Notification getNotificationById(@PathVariable(value = "id") Long id) {
        log.debug("New notification CRUD request. Action = GET. id = {}",id);
        return notificationRepository.findById(id).orElseThrow(() -> new NotificationNotFoundException(id));
    }

    @GetMapping(value = "/")
    @ResponseBody
    public List<Notification> getNotificationByUserId(@RequestParam(value = "userId") Long id) {
        log.debug("New notification CRUD request. Action = GET. user_id = {}",id);
        return notificationRepository.findAllByUserIdOrderByTimestampDesc(id);
    }

    @DeleteMapping(value = "/")
    public void deleteNotification(@RequestBody NotificationDTO notification) {
        log.debug("New notification CRUD request. Action = DELETE. id = {}",notification.getId());
        Notification persistentNotification = new Notification();
        persistentNotification.setId(notification.getId());
        persistentNotification.setMessage(notification.getMessage());
        persistentNotification.setType(notification.getType());
        persistentNotification.setUserId(notification.getUserId());
        log.info("Notification entry deleted. id = {}",persistentNotification.getId());
        notificationRepository.delete(persistentNotification);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteNotificationById(@PathVariable(value = "id") Long id) {
        log.debug("New notification CRUD request. Action = DELETE. id = {}",id);
        notificationRepository.deleteById(id);
        log.info("Notification entry deleted. id = {}",id);
    }

    @PostMapping(value = "/")
    public void createNotification(@RequestBody NotificationDTO notification) {
        log.debug("New notification CRUD request. Action = SAVE. id = {}",notification.getId());
        Notification persistentNotification = new Notification();
        persistentNotification.setId(notification.getId());
        persistentNotification.setMessage(notification.getMessage());
        persistentNotification.setType(notification.getType());
        persistentNotification.setUserId(notification.getUserId());
        log.debug("Notification entry saved. id = {}",persistentNotification.getId());
        notificationRepository.save(persistentNotification);
    }

    @Autowired
    private UserServiceKafkaMessageHandler userServiceKafkaMessageHandler;

    @PostMapping(value = "/trigger_kafka_consumer")
    public void triggerKafkaConsumer(@RequestBody WinePriceUpdatedWithTokensEvent event){
        userServiceKafkaMessageHandler.handle(event);
    }
}
