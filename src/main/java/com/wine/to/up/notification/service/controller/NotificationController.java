package com.wine.to.up.notification.service.controller;

import com.wine.to.up.notification.service.domain.entity.Notification;
import com.wine.to.up.notification.service.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notification")
@Validated
@Slf4j
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @PutMapping(value = "/")
    public void updateNotification(@RequestBody Notification notification) {
        notificationRepository.save(notification);
    }

    @GetMapping(value = "/{id}")
    public @ResponseBody
    Optional<Notification> getNotificationById(@PathVariable(value = "id") Long id) {
        return notificationRepository.findById(id);
    }

    @GetMapping(value = "/")
    public @ResponseBody
    List<Notification> getNotificationByUserId(@RequestParam(value = "userId") Long id) {
        return notificationRepository.findAllByUserId(id);
    }

    @DeleteMapping(value = "/")
    public void deleteNotification(@RequestBody Notification notification) {
        notificationRepository.delete(notification);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteNotificationById(@PathVariable(value = "id") Long id) {
        notificationRepository.deleteById(id);
    }

    @PostMapping(value = "/")
    public void createNotification(@RequestBody Notification notification) {
        notificationRepository.save(notification);
    }
}
