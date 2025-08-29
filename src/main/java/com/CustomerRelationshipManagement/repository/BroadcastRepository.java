package com.CustomerRelationshipManagement.repository;

import com.CustomerRelationshipManagement.entity.BroadcastEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BroadcastRepository extends JpaRepository<BroadcastEntity, Long> {

    long countByStatus(String status);

    List<BroadcastEntity> findByStatusAndScheduledTimeBefore(String status, LocalDateTime scheduledTime);

    // ✨ FIX: The query is now simplified and correct.
    // It only fetches messages where the user's email is actually in the recipient list.
    @Query("SELECT b FROM BroadcastEntity b WHERE b.status = 'Sent' AND b.scheduledTime <= CURRENT_TIMESTAMP AND :email MEMBER OF b.recipients")
    List<BroadcastEntity> findMessagesForUser(@Param("email") String email);
}