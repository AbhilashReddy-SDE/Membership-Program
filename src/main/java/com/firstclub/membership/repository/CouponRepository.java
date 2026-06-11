package com.firstclub.membership.repository;

import com.firstclub.membership.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    long countByUserIdAndTierIdAndYearMonthKey(Long userId, Long tierId, int yearMonthKey);
}
