package com.wine.to.up.notification.service.controller;

import com.wine.to.up.notification.service.domain.entity.Notification;
import com.wine.to.up.notification.service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
@Validated
@Slf4j
public class NotificationController {

    private NotificationRepository notificationRepository;

    @Autowired
    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @RequestMapping(value = "/", method = RequestMethod.PATCH)
    public void updateNotification(@RequestBody Notification notification) {
        notificationRepository.update(notification);
    }

    @GetMapping(value = "/?id={id}")
    public @ResponseBody
    Notification getNotificationById(@PathVariable(value = "id") Long id) {
        return notificationRepository.getById(id);
    }

    @GetMapping(value = "/?userid={id}")
    public @ResponseBody
    List<Notification> getNotificationByUserId(@PathVariable(value = "id") Long id) {
        return notificationRepository.getByUserId(id);
    }

    @DeleteMapping(value = "/")
    public void deleteNotification(@RequestBody Notification notification) {
        notificationRepository.delete(notification);
    }

    @PutMapping
    public void putNotification(@RequestBody Notification notification) {
        notificationRepository.save(notification);
    }
}
