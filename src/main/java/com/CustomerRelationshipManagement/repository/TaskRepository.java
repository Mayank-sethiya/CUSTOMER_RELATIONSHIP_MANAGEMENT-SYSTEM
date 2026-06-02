
package com.CustomerRelationshipManagement.repository;

import com.CustomerRelationshipManagement.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    // Using custom query to ensure exact email matching
    @Query("SELECT t FROM TaskEntity t WHERE CONCAT(',', t.assignedTo, ',') LIKE %:emailWithComma%")
    List<TaskEntity> findByAssignedToContaining(@Param("emailWithComma") String email);


    // Alternative method for debugging - finds all tasks
    @Query("SELECT t FROM TaskEntity t")
    List<TaskEntity> findAllTasks();

    @Query("SELECT t FROM TaskEntity t WHERE " +
            "CONCAT(',', t.assignedTo, ',') LIKE %:emailWithComma% " +
            "AND t.status <> 'deleted' " +
            "AND t.assignedBy = :assignedBy")
    List<TaskEntity> findByAssignedToAndAssignedByAndStatusNotDeleted(@Param("emailWithComma") String email,
                                                                      @Param("assignedBy") String assignedBy);


    List<TaskEntity> findTop10ByStatusNotOrderByCreatedAtDesc(String status);


}
