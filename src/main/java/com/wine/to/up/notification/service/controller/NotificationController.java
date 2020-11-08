package com.wine.to.up.notification.service.controller;

import com.wine.to.up.notification.service.domain.entity.Notification;
import com.wine.to.up.notification.service.DTO.NotificationDTO;
import com.wine.to.up.notification.service.exceptions.NotificationNotFoundException;
import com.wine.to.up.notification.service.repository.NotificationRepository;
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
        Notification persistentNotification = new Notification();
        persistentNotification.setId(notification.getId());
        persistentNotification.setMessage(notification.getMessage());
        persistentNotification.setType(notification.getType());
        persistentNotification.setUserId(notification.getUserId());
        notificationRepository.save(persistentNotification);
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public Notification getNotificationById(@PathVariable(value = "id") Long id) {
        return notificationRepository.findById(id).orElseThrow(() -> new NotificationNotFoundException(id));
    }

    @GetMapping(value = "/")
    @ResponseBody
    public List<Notification> getNotificationByUserId(@RequestParam(value = "userId") Long id) {
        return notificationRepository.findAllByUserId(id);
    }

    @DeleteMapping(value = "/")
    public void deleteNotification(@RequestBody NotificationDTO notification) {
        Notification persistentNotification = new Notification();
        persistentNotification.setId(notification.getId());
        persistentNotification.setMessage(notification.getMessage());
        persistentNotification.setType(notification.getType());
        persistentNotification.setUserId(notification.getUserId());
        notificationRepository.delete(persistentNotification);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteNotificationById(@PathVariable(value = "id") Long id) {
        notificationRepository.deleteById(id);
    }

    @PostMapping(value = "/")
    public void createNotification(@RequestBody NotificationDTO notification) {
        Notification persistentNotification = new Notification();
        persistentNotification.setId(notification.getId());
        persistentNotification.setMessage(notification.getMessage());
        persistentNotification.setType(notification.getType());
        persistentNotification.setUserId(notification.getUserId());
        notificationRepository.save(persistentNotification);
    }
}
