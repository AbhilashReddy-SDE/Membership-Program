package com.firstclub.membership.dto.response;

import com.firstclub.membership.entity.Tier;
import com.firstclub.membership.entity.TierProgressionRule;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

public record TierResponse(
        Long id,
        String code,
        String name,
        int rank,
        BigDecimal discountPercent,
        boolean fastDelivery,
        int couponsGenerated,
        BigDecimal freeDeliveryThreshold,
        boolean exclusiveDeals,
        boolean earlyAccess,
        Map<String, String> benefits,
        TierProgressionRuleResponse rule
) {
    public static TierResponse from(Tier tier) {
        Map<String, String> benefitMap = tier.getBenefits().stream()
                .collect(Collectors.toMap(b -> b.getBenefitKey(), b -> b.getBenefitValue()));

        TierProgressionRuleResponse rule = tier.getProgressionRules().stream()
                .filter(TierProgressionRule::isActive)
                .findFirst()
                .map(TierProgressionRuleResponse::from)
                .orElse(null);

        return new TierResponse(
                tier.getId(), tier.getCode(), tier.getName(), tier.getRank(),
                tier.getDiscountPercent(), tier.isFastDelivery(), tier.getCouponsGenerated(),
                tier.getFreeDeliveryThreshold(), tier.isExclusiveDeals(), tier.isEarlyAccess(),
                benefitMap, rule
        );
    }
}
