package com.firstclub.membership.dto.response;

import com.firstclub.membership.entity.TierProgressionRule;

import java.math.BigDecimal;

public record TierProgressionRuleResponse(
        int minMonthlyOrders,
        BigDecimal minMonthlySpend
) {
    public static TierProgressionRuleResponse from(TierProgressionRule rule) {
        return new TierProgressionRuleResponse(rule.getMinMonthlyOrders(), rule.getMinMonthlySpend());
    }
}
