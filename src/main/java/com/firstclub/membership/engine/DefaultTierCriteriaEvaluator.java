package com.firstclub.membership.engine;

import com.firstclub.membership.entity.Tier;
import com.firstclub.membership.entity.TierProgressionRule;
import com.firstclub.membership.entity.UserMonthlyStats;
import org.springframework.stereotype.Component;

/**
 * Default evaluator: a tier is satisfied when the user meets the minimum monthly
 * order count AND minimum monthly spend defined in the tier's active progression rule.
 */
@Component
public class DefaultTierCriteriaEvaluator implements TierCriteriaEvaluator {

    @Override
    public boolean isSatisfied(Tier tier, UserMonthlyStats stats) {
        return tier.getProgressionRules().stream()
                .filter(TierProgressionRule::isActive)
                .anyMatch(rule ->
                        stats.getOrderCount() >= rule.getMinMonthlyOrders()
                        && stats.getTotalSpend().compareTo(rule.getMinMonthlySpend()) >= 0
                );
    }
}
