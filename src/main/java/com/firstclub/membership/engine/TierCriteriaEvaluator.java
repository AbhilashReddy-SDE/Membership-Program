package com.firstclub.membership.engine;

import com.firstclub.membership.entity.Tier;
import com.firstclub.membership.entity.UserMonthlyStats;

/**
 * Strategy interface for evaluating whether a user's monthly stats satisfy a tier's progression criteria.
 * Implement and register as a Spring bean to add new criteria without modifying existing code.
 */
public interface TierCriteriaEvaluator {
    boolean isSatisfied(Tier tier, UserMonthlyStats stats);
}
