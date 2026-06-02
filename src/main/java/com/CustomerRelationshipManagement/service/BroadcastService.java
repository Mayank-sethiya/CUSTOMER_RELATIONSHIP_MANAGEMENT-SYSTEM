package com.CustomerRelationshipManagement.service;

import com.CustomerRelationshipManagement.entity.BroadcastEntity;
import com.CustomerRelationshipManagement.entity.UserNotification;
import com.CustomerRelationshipManagement.repository.BroadcastRepository;
import com.CustomerRelationshipManagement.repository.UserNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BroadcastService {

    @Autowired
    private BroadcastRepository repository;

    @Autowired
    private UserNotificationRepository userNotificationRepo;

    public BroadcastEntity sendMessage(BroadcastEntity message) {
        message.setCreatedAt(LocalDateTime.now());

        if (message.getScheduledTime() != null && message.getScheduledTime().isAfter(LocalDateTime.now())) {
            message.setStatus("Pending");
        } else {
            if (message.getScheduledTime() == null) {
                message.setScheduledTime(LocalDateTime.now());
            }
            message.setStatus("Sent");
        }

        BroadcastEntity saved = repository.save(message);

        // ✅ Create UserNotification entries for each recipient
        if (saved.getRecipients() != null) {
            for (String email : saved.getRecipients()) {
                UserNotification un = UserNotification.builder()
                        .userEmail(email)
                        .broadcastId(saved.getId())
                        .read(false)
                        .cleared(false)
                        .build();
                userNotificationRepo.save(un);
            }
        }

        return saved;
    }

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void processScheduledMessages() {
        List<BroadcastEntity> pendingMessages =
                repository.findByStatusAndScheduledTimeBefore("Pending", LocalDateTime.now());
        for (BroadcastEntity msg : pendingMessages) {
            msg.setStatus("Sent");
            repository.save(msg);

            // ✅ When scheduled message is sent, also create UserNotifications
            if (msg.getRecipients() != null) {
                for (String email : msg.getRecipients()) {
                    UserNotification un = UserNotification.builder()
                            .userEmail(email)
                            .broadcastId(msg.getId())
                            .read(false)
                            .cleared(false)
                            .build();
                    userNotificationRepo.save(un);
                }
            }
        }
    }

    public List<BroadcastEntity> getAllMessages() {
        return repository.findAll();
    }

    @Transactional
    public void deleteMessageById(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public void deleteAllMessages() {
        repository.deleteAll();
    }

    public long getMessageCount() {
        return repository.countByStatus("Sent");
    }

    public long countPendingMessages() {
        return repository.countByStatus("Pending");
    }

    @Transactional(readOnly = true)
    public List<BroadcastEntity> getMessagesForUser(String email) {
        return repository.findMessagesForUser(email);
    }
}
