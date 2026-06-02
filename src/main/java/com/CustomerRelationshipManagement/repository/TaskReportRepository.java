package com.CustomerRelationshipManagement.repository;

import com.CustomerRelationshipManagement.entity.TaskReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskReportRepository extends JpaRepository<TaskReportEntity, Long> {
        @Query("SELECT r FROM TaskReportEntity r WHERE " +
                "(:userEmail IS NULL OR LOWER(r.userEmail) LIKE LOWER(CONCAT('%', :userEmail, '%'))) AND " +
                "(:userId IS NULL OR CAST(r.taskId AS string) LIKE CONCAT('%', :userId, '%'))")
        List<TaskReportEntity> searchReports(@Param("userEmail") String userEmail, @Param("userId") String userId);

    }


