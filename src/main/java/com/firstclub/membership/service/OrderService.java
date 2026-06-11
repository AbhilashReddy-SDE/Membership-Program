package com.firstclub.membership.service;

import com.firstclub.membership.dto.response.OrderResponse;
import com.firstclub.membership.engine.TierEvaluationEngine;
import com.firstclub.membership.entity.*;
import com.firstclub.membership.entity.enums.EventType;
import com.firstclub.membership.entity.enums.MembershipStatus;
import com.firstclub.membership.exception.EntityNotFoundException;
import com.firstclub.membership.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final UserMonthlyStatsRepository statsRepository;
    private final MembershipRepository membershipRepository;
    private final CouponService couponService;
    private final TierEvaluationEngine tierEvaluationEngine;

    @Transactional
    public OrderResponse recordOrder(Long userId, java.math.BigDecimal orderValue) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        Order order = new Order();
        order.setUser(user);
        order.setOrderValue(orderValue);
        orderRepository.save(order);

        int yearMonthKey = currentYearMonthKey();
        UserMonthlyStats stats = upsertStats(user, yearMonthKey, orderValue);

        Optional<Membership> activeMembership =
                membershipRepository.findByUserIdAndStatus(userId, MembershipStatus.ACTIVE);
        activeMembership.ifPresent(m -> reevaluateTier(m, stats, yearMonthKey));

        return OrderResponse.from(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getUserOrders(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found: " + userId);
        }
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId)
                .stream()
                .map(OrderResponse::from)
                .toList();
    }

    private UserMonthlyStats upsertStats(User user, int yearMonthKey, java.math.BigDecimal orderValue) {
        UserMonthlyStats stats = statsRepository
                .findByUserIdAndYearMonthKey(user.getId(), yearMonthKey)
                .orElseGet(() -> {
                    UserMonthlyStats s = new UserMonthlyStats();
                    s.setUser(user);
                    s.setYearMonthKey(yearMonthKey);
                    return s;
                });
        stats.setOrderCount(stats.getOrderCount() + 1);
        stats.setTotalSpend(stats.getTotalSpend().add(orderValue));
        return statsRepository.save(stats);
    }

    private void reevaluateTier(Membership membership, UserMonthlyStats stats, int yearMonthKey) {
        tierEvaluationEngine.evaluate(stats).ifPresent(newTier -> {
            Tier currentTier = membership.getTier();
            if (!newTier.getId().equals(currentTier.getId())) {
                membership.setTier(newTier);
                membershipRepository.save(membership);
                couponService.generateCoupons(membership.getUser(), newTier, yearMonthKey);
            }
        });
    }

    private int currentYearMonthKey() {
        LocalDate now = LocalDate.now();
        return now.getYear() * 100 + now.getMonthValue();
    }
}
