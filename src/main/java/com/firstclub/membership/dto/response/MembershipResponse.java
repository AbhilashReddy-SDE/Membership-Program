package com.firstclub.membership.dto.response;

import com.firstclub.membership.entity.Membership;
import com.firstclub.membership.entity.enums.MembershipStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MembershipResponse(
        Long id,
        Long userId,
        PlanResponse plan,
        TierResponse tier,
        MembershipStatus status,
        LocalDate startDate,
        LocalDate endDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static MembershipResponse from(Membership m) {
        return new MembershipResponse(
                m.getId(),
                m.getUser().getId(),
                PlanResponse.from(m.getPlan()),
                TierResponse.from(m.getTier()),
                m.getStatus(),
                m.getStartDate(),
                m.getEndDate(),
                m.getCreatedAt(),
                m.getUpdatedAt()
        );
    }
}
