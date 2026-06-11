package com.firstclub.membership.service;

import com.firstclub.membership.dto.response.MembershipResponse;
import com.firstclub.membership.dto.response.TierResponse;
import com.firstclub.membership.entity.*;
import com.firstclub.membership.entity.enums.MembershipStatus;
import com.firstclub.membership.exception.BusinessRuleException;
import com.firstclub.membership.exception.EntityNotFoundException;
import com.firstclub.membership.repository.MembershipRepository;
import com.firstclub.membership.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;
    private final MembershipPlanService planService;
    private final TierService tierService;

    @Transactional
    public MembershipResponse subscribe(Long userId, String planCode) {
        User user = requireUser(userId);
        if (membershipRepository.existsByUserIdAndStatus(userId, MembershipStatus.ACTIVE)) {
            throw new BusinessRuleException("User already has an active membership");
        }
        MembershipPlan plan = planService.requireActiveByCode(planCode);
        Tier lowestTier = tierService.requireLowestActiveTier();

        Membership m = new Membership();
        m.setUser(user);
        m.setPlan(plan);
        m.setTier(lowestTier);
        m.setStatus(MembershipStatus.ACTIVE);
        m.setStartDate(LocalDate.now());
        m.setEndDate(LocalDate.now().plusMonths(plan.getDurationMonths()));
        membershipRepository.save(m);

        return MembershipResponse.from(m);
    }

    @Transactional
    public MembershipResponse upgrade(Long userId, String planCode) {
        Membership m = requireActive(userId);
        MembershipPlan newPlan = planService.requireActiveByCode(planCode);

        if (newPlan.getDurationMonths() <= m.getPlan().getDurationMonths()) {
            throw new IllegalArgumentException("Upgrade target must have a longer duration than the current plan");
        }

        Long oldPlanId = m.getPlan().getId();
        m.setPlan(newPlan);
        m.setEndDate(m.getStartDate().plusMonths(newPlan.getDurationMonths()));
        membershipRepository.save(m);

        return MembershipResponse.from(m);
    }

    @Transactional
    public MembershipResponse downgrade(Long userId, String planCode) {
        Membership m = requireActive(userId);
        MembershipPlan newPlan = planService.requireActiveByCode(planCode);

        if (newPlan.getDurationMonths() >= m.getPlan().getDurationMonths()) {
            throw new IllegalArgumentException("Downgrade target must have a shorter duration than the current plan");
        }

        Long oldPlanId = m.getPlan().getId();
        m.setPlan(newPlan);
        m.setEndDate(m.getStartDate().plusMonths(newPlan.getDurationMonths()));
        membershipRepository.save(m);

        return MembershipResponse.from(m);
    }

    @Transactional
    public MembershipResponse cancel(Long userId) {
        Membership m = requireActive(userId);
        m.setStatus(MembershipStatus.CANCELLED);
        membershipRepository.save(m);

        return MembershipResponse.from(m);
    }

    @Transactional(readOnly = true)
    public MembershipResponse getActiveMembership(Long userId) {
        requireUser(userId);
        return MembershipResponse.from(requireActive(userId));
    }

    @Transactional(readOnly = true)
    public LocalDate getExpiryDate(Long userId) {
        requireUser(userId);
        return requireActive(userId).getEndDate();
    }

    @Transactional(readOnly = true)
    public TierResponse getCurrentTier(Long userId) {
        requireUser(userId);
        return TierResponse.from(requireActive(userId).getTier());
    }

    public Membership requireActive(Long userId) {
        return membershipRepository.findByUserIdAndStatus(userId, MembershipStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("No active membership for user: " + userId));
    }

    private User requireUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    }
}
