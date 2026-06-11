package com.firstclub.membership.engine;

import com.firstclub.membership.entity.Tier;
import com.firstclub.membership.entity.UserMonthlyStats;
import com.firstclub.membership.repository.TierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Evaluates the highest tier a user qualifies for given their current monthly stats.
 * Iterates active tiers in descending rank order and returns the first whose criteria are met.
 */
@Component
@RequiredArgsConstructor
public class TierEvaluationEngine {

    private final TierRepository tierRepository;
    private final List<TierCriteriaEvaluator> evaluators;

    public Optional<Tier> evaluate(UserMonthlyStats stats) {
        List<Tier> tiersDescending = tierRepository.findAllByActiveTrueOrderByRankDesc();
        return tiersDescending.stream()
                .filter(tier -> evaluators.stream().allMatch(e -> e.isSatisfied(tier, stats)))
                .findFirst();
    }
}
