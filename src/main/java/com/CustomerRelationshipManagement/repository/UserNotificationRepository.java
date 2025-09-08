package com.CustomerRelationshipManagement.repository;

import com.CustomerRelationshipManagement.entity.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {
    List<UserNotification> findByUserEmailAndClearedFalse(String email);
    UserNotification findByUserEmailAndBroadcastId(String email, Long broadcastId);
}
