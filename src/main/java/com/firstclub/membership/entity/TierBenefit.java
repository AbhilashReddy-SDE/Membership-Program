package com.firstclub.membership.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tier_benefit",
        uniqueConstraints = @UniqueConstraint(columnNames = {"tier_id", "benefit_key"}))
@Getter
@Setter
@NoArgsConstructor
public class TierBenefit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tier_id", nullable = false)
    private Tier tier;

    @Column(nullable = false, length = 60)
    private String benefitKey;

    @Column(nullable = false, length = 255)
    private String benefitValue;
}
