package com.firstclub.membership.repository;

import com.firstclub.membership.entity.Tier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TierRepository extends JpaRepository<Tier, Long> {
    List<Tier> findAllByActiveTrueOrderByRankAsc();
    List<Tier> findAllByActiveTrueOrderByRankDesc();
    Optional<Tier> findFirstByActiveTrueOrderByRankAsc();
}
