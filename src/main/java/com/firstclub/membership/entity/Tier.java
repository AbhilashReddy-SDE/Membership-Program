package com.firstclub.membership.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tier")
@Getter
@Setter
@NoArgsConstructor
public class Tier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @Column(nullable = false, length = 60)
    private String name;

    @Column(nullable = false, unique = true)
    private int rank;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal discountPercent = BigDecimal.ZERO;

    @Column(nullable = false)
    private boolean fastDelivery = false;

    @Column(nullable = false)
    private int couponsGenerated = 0;

    @Column(precision = 10, scale = 2)
    private BigDecimal freeDeliveryThreshold;

    @Column(nullable = false)
    private boolean exclusiveDeals = false;

    @Column(nullable = false)
    private boolean earlyAccess = false;

    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "tier", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TierBenefit> benefits = new ArrayList<>();

    @OneToMany(mappedBy = "tier", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TierProgressionRule> progressionRules = new ArrayList<>();
}
