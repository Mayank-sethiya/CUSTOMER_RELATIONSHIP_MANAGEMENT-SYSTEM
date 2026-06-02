package com.CustomerRelationshipManagement.service;

import com.CustomerRelationshipManagement.entity.UserNotification;
import com.CustomerRelationshipManagement.repository.UserNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserNotificationService {

    @Autowired
    private UserNotificationRepository repo;

    public List<UserNotification> getNotificationsForUser(String email) {
        return repo.findByUserEmailAndClearedFalse(email);
    }

    public void markAsRead(String email, Long broadcastId) {
        UserNotification un = repo.findByUserEmailAndBroadcastId(email, broadcastId);
        if (un != null) {
            un.setRead(true);
            un.setReadAt(LocalDateTime.now());
            repo.save(un);
        }
    }

    public void clearNotification(String email, Long broadcastId) {
        UserNotification un = repo.findByUserEmailAndBroadcastId(email, broadcastId);
        if (un != null) {
            un.setCleared(true);
            un.setClearedAt(LocalDateTime.now());
            repo.save(un);
        }
    }
}
