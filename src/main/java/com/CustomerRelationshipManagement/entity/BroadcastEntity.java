package com.CustomerRelationshipManagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "broadcast_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BroadcastEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private String subject;

    // This annotation tells PostgreSQL to use the TEXT type, which supports long content
    // and avoids the Large Object error.
    @Column(columnDefinition = "TEXT")
    private String content;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "broadcast_recipients", joinColumns = @JoinColumn(name = "broadcast_id"))
    // This annotation is also important for the list of recipients
    @Column(name = "recipient", columnDefinition = "TEXT")
    private List<String> recipients;

    private String status;
    private LocalDateTime scheduledTime;
    private LocalDateTime createdAt;
}