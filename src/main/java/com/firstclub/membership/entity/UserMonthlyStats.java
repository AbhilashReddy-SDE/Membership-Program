package com.firstclub.membership.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "user_monthly_stats",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "year_month_key"}))
@Getter
@Setter
@NoArgsConstructor
public class UserMonthlyStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private int yearMonthKey;

    @Column(nullable = false)
    private int orderCount = 0;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal totalSpend = BigDecimal.ZERO;

    @Version
    private Long version;
}
