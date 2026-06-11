package com.firstclub.membership.repository;

import com.firstclub.membership.entity.UserMonthlyStats;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserMonthlyStatsRepository extends JpaRepository<UserMonthlyStats, Long> {
    Optional<UserMonthlyStats> findByUserIdAndYearMonthKey(Long userId, int yearMonthKey);

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Query("SELECT s FROM UserMonthlyStats s WHERE s.user.id = :userId AND s.yearMonthKey = :key")
    Optional<UserMonthlyStats> findByUserIdAndYearMonthKeyForUpdate(
            @Param("userId") Long userId, @Param("key") int yearMonthKey);
}
