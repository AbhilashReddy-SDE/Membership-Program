package com.firstclub.membership.service;

import com.firstclub.membership.entity.Coupon;
import com.firstclub.membership.entity.Tier;
import com.firstclub.membership.entity.User;
import com.firstclub.membership.entity.enums.CouponStatus;
import com.firstclub.membership.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    /**
     * Generates {@code tier.couponsGenerated} coupons for the given user and period.
     * Idempotent: skips generation if coupons already exist for (user, tier, period).
     */
    @Transactional
    public List<Coupon> generateCoupons(User user, Tier tier, int yearMonthKey) {
        if (tier.getCouponsGenerated() <= 0) {
            return List.of();
        }
        long existing = couponRepository.countByUserIdAndTierIdAndYearMonthKey(
                user.getId(), tier.getId(), yearMonthKey);
        if (existing >= tier.getCouponsGenerated()) {
            return List.of();
        }

        List<Coupon> coupons = new ArrayList<>();
        long toGenerate = tier.getCouponsGenerated() - existing;
        for (int i = 0; i < toGenerate; i++) {
            Coupon c = new Coupon();
            c.setUser(user);
            c.setTier(tier);
            c.setCode(generateCode());
            c.setYearMonthKey(yearMonthKey);
            c.setStatus(CouponStatus.ACTIVE);
            coupons.add(couponRepository.save(c));
        }
        return coupons;
    }

    private String generateCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }
}
