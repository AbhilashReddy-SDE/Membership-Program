package com.firstclub.membership.config;

import com.firstclub.membership.entity.*;
import com.firstclub.membership.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final MembershipPlanRepository planRepository;
    private final TierRepository tierRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void run(String... args) {
        seedPlans();
        seedTiers();
        seedUsers();
        log.info("Data seeding complete.");
    }

    private void seedPlans() {
        if (planRepository.count() > 0) return;

        planRepository.saveAll(List.of(
                plan("MONTHLY", "Monthly", 1, new BigDecimal("199.00")),
                plan("QUARTERLY", "Quarterly", 3, new BigDecimal("499.00")),
                plan("YEARLY", "Yearly", 12, new BigDecimal("1499.00"))
        ));
        log.info("Seeded 3 membership plans.");
    }

    private void seedTiers() {
        if (tierRepository.count() > 0) return;

        Tier silver = new Tier();
        silver.setCode("SILVER");
        silver.setName("Silver");
        silver.setRank(1);
        silver.setDiscountPercent(BigDecimal.ZERO);
        silver.setFastDelivery(false);
        silver.setCouponsGenerated(1);
        silver.setExclusiveDeals(false);
        silver.setEarlyAccess(false);
        tierRepository.save(silver);
        addBenefit(silver, "discount", "0%");
        addRule(silver, 0, BigDecimal.ZERO);

        Tier gold = new Tier();
        gold.setCode("GOLD");
        gold.setName("Gold");
        gold.setRank(2);
        gold.setDiscountPercent(new BigDecimal("10.00"));
        gold.setFastDelivery(true);
        gold.setCouponsGenerated(2);
        gold.setFreeDeliveryThreshold(new BigDecimal("500.00"));
        gold.setExclusiveDeals(false);
        gold.setEarlyAccess(false);
        tierRepository.save(gold);
        addBenefit(gold, "discount", "10%");
        addBenefit(gold, "fast_delivery", "true");
        addBenefit(gold, "free_delivery_above", "500");
        addRule(gold, 10, new BigDecimal("10000.00"));

        Tier diamond = new Tier();
        diamond.setCode("DIAMOND");
        diamond.setName("Diamond");
        diamond.setRank(3);
        diamond.setDiscountPercent(new BigDecimal("20.00"));
        diamond.setFastDelivery(true);
        diamond.setCouponsGenerated(5);
        diamond.setFreeDeliveryThreshold(BigDecimal.ZERO);
        diamond.setExclusiveDeals(true);
        diamond.setEarlyAccess(true);
        tierRepository.save(diamond);
        addBenefit(diamond, "discount", "20%");
        addBenefit(diamond, "fast_delivery", "true");
        addBenefit(diamond, "free_delivery_above", "0");
        addBenefit(diamond, "exclusive_deals", "true");
        addBenefit(diamond, "early_access", "true");
        addRule(diamond, 20, new BigDecimal("25000.00"));

        log.info("Seeded 3 tiers: SILVER, GOLD, DIAMOND.");
    }

    private void seedUsers() {
        if (userRepository.count() > 0) return;

        User alice = new User();
        alice.setName("Alice");
        alice.setEmail("alice@example.com");

        User bob = new User();
        bob.setName("Bob");
        bob.setEmail("bob@example.com");

        User carol = new User();
        carol.setName("Carol");
        carol.setEmail("carol@example.com");

        userRepository.saveAll(List.of(alice, bob, carol));
        log.info("Seeded 3 demo users (IDs 1, 2, 3).");
    }

    private MembershipPlan plan(String code, String name, int months, BigDecimal price) {
        MembershipPlan p = new MembershipPlan();
        p.setCode(code);
        p.setName(name);
        p.setDurationMonths(months);
        p.setPrice(price);
        return p;
    }

    private void addBenefit(Tier tier, String key, String value) {
        TierBenefit b = new TierBenefit();
        b.setTier(tier);
        b.setBenefitKey(key);
        b.setBenefitValue(value);
        tier.getBenefits().add(b);
        tierRepository.save(tier);
    }

    private void addRule(Tier tier, int minOrders, BigDecimal minSpend) {
        TierProgressionRule r = new TierProgressionRule();
        r.setTier(tier);
        r.setMinMonthlyOrders(minOrders);
        r.setMinMonthlySpend(minSpend);
        tier.getProgressionRules().add(r);
        tierRepository.save(tier);
    }
}
