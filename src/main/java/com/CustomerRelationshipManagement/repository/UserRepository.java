package com.CustomerRelationshipManagement.repository;

import com.CustomerRelationshipManagement.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // ... your existing methods ...
    UserEntity findByUsernameAndPassword(String username, String password);
    UserEntity findByUsername(String username);
    @Query("SELECT u FROM UserEntity u WHERE u.username LIKE %:keyword% OR u.email LIKE %:keyword%")
    List<UserEntity> searchUsers(@Param("keyword") String keyword);
    long countByCreatedAtAfter(LocalDateTime time);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByPhonenumber(String phonenumber);


    // THE ONLY CHANGE IS ON THE "LEFT JOIN" LINE BELOW
    @Query(value = """
        SELECT
            to_char(month_series.month, 'Mon') AS month,
            COALESCE(count(u.created_at), 0)::int AS count
        FROM
            generate_series(
                date_trunc('month', now()) - interval '5 months',
                date_trunc('month', now()),
                '1 month'
            ) AS month_series(month)
        LEFT JOIN
            user_entity u ON date_trunc('month', u.created_at) = month_series.month -- FIXED: Changed "users" to "user_entity"
        GROUP BY
            month_series.month
        ORDER BY
            month_series.month ASC
    """, nativeQuery = true)
    List<MonthlySignup> findMonthlySignups();



}