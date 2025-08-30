package com.CustomerRelationshipManagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime; // Import this

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contacts")
public class ContactEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String company;
    private String status;

    // ✅ FIX 1: Changed field name to match the form data
    @Column(columnDefinition = "TEXT")
    private String problemDescription;

    // ✅ FIX 2: Changed data type to handle dates properly
    private LocalDateTime createdAt;
}