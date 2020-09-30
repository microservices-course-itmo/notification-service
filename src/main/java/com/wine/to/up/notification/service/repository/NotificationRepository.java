package com.wine.to.up.notification.service.repository;

import com.wine.to.up.notification.service.domain.entity.Notification;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class NotificationRepository  {

    @PersistenceContext
    private EntityManager em;


    /**
     * Создаёт или обновляет оповещение
     * @param notification - оповещение для создания или обновления
     */
    public void save(Notification notification) {
        if (notification.getId() == 0) {
            em.persist(notification);
        } else {
            em.merge(notification);
        }
        System.out.println("Notification saved with id: " + notification.getId());
    }

    /**
     * Удаляет оповещение
     * @param notification - оповещение для удаления
     */
    public void delete(Notification notification) {
        Notification mergedNotification = em.merge(notification);
        em.remove(mergedNotification);
        System.out.println("Notification with id: " + mergedNotification.getId() + " deleted successfully");
    }
}