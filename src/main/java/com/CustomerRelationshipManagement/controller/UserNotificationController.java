package com.CustomerRelationshipManagement.controller;

import com.CustomerRelationshipManagement.entity.UserNotification;
import com.CustomerRelationshipManagement.service.UserNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class UserNotificationController {

    @Autowired
    private UserNotificationService service;

    @GetMapping("/for-user")
    public List<UserNotification> getNotificationsForUser(@RequestParam String email) {
        return service.getNotificationsForUser(email);
    }

    @PostMapping("/mark-read")
    public void markAsRead(@RequestParam String email, @RequestParam Long broadcastId) {
        service.markAsRead(email, broadcastId);
    }

    @PostMapping("/clear")
    public void clearNotification(@RequestParam String email, @RequestParam Long broadcastId) {
        service.clearNotification(email, broadcastId);
    }
}
