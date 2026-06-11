package com.firstclub.membership.service;

import com.firstclub.membership.dto.response.PlanResponse;
import com.firstclub.membership.entity.MembershipPlan;
import com.firstclub.membership.exception.EntityNotFoundException;
import com.firstclub.membership.repository.MembershipPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MembershipPlanService {

    private final MembershipPlanRepository planRepository;

    @Transactional(readOnly = true)
    public List<PlanResponse> getAllActivePlans() {
        return planRepository.findAllByActiveTrueOrderByDurationMonthsAsc()
                .stream()
                .map(PlanResponse::from)
                .toList();
    }

    public MembershipPlan requireActiveByCode(String code) {
        return planRepository.findByCodeAndActiveTrue(code)
                .orElseThrow(() -> new EntityNotFoundException("Active plan not found: " + code));
    }
}
