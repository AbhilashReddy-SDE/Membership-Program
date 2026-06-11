package com.firstclub.membership.repository;

import com.firstclub.membership.entity.Membership;
import com.firstclub.membership.entity.enums.MembershipStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Optional<Membership> findByUserIdAndStatus(Long userId, MembershipStatus status);
    boolean existsByUserIdAndStatus(Long userId, MembershipStatus status);
}
