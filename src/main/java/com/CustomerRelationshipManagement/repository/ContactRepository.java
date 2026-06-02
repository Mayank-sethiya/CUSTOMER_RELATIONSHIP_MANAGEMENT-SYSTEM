package com.CustomerRelationshipManagement.repository;

import com.CustomerRelationshipManagement.entity.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
    ContactEntity findByEmail(String email);

    long count();


}
