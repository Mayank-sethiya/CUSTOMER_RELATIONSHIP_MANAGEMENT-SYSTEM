package com.CustomerRelationshipManagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;

    private Long broadcastId;  // reference to BroadcastEntity.id

    private boolean read = false;
    private boolean cleared = false;

    private LocalDateTime readAt;
    private LocalDateTime clearedAt;
}
