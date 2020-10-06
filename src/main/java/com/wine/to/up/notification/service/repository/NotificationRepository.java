package com.wine.to.up.notification.service.repository;

import com.wine.to.up.notification.service.domain.entity.Notification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class NotificationRepository  {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(Notification notification) {
        if (notification.getId() == 0) {
            entityManager.persist(notification);
        } else {
            entityManager.merge(notification);
        }
    }

    public void delete(Notification notification) {
        final Notification mergedNotification = entityManager.merge(notification);
        entityManager.remove(mergedNotification);
    }

    public void deleteById(Long id) {
        final Notification mergedNotification = entityManager.merge(getById(id));
        entityManager.remove(mergedNotification;
    }

    public Notification getById(Long id) {
        return entityManager.find(Notification.class, id);
    }

    public void update(Notification notification) {
        entityManager.merge(notification);
    }

    public List<Notification> getByUserId(Long userId) {
        return entityManager.createQuery("SELECT n FROM Notification n WHERE n.userId = ?1")
                .setParameter(1, userId)
                .getResultList();
    }
}