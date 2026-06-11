package com.firstclub.membership.dto.response;

import com.firstclub.membership.entity.MembershipPlan;

import java.math.BigDecimal;

public record PlanResponse(
        Long id,
        String code,
        String name,
        int durationMonths,
        BigDecimal price
) {
    public static PlanResponse from(MembershipPlan plan) {
        return new PlanResponse(plan.getId(), plan.getCode(), plan.getName(),
                plan.getDurationMonths(), plan.getPrice());
    }
}
