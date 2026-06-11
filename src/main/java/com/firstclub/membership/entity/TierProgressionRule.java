package com.firstclub.membership.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "tier_progression_rule")
@Getter
@Setter
@NoArgsConstructor
public class TierProgressionRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tier_id", nullable = false)
    private Tier tier;

    @Column(nullable = false)
    private int minMonthlyOrders = 0;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal minMonthlySpend = BigDecimal.ZERO;

    @Column(nullable = false)
    private boolean active = true;
}
