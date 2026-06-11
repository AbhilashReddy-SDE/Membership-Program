package com.firstclub.membership.service;

import com.firstclub.membership.dto.response.TierResponse;
import com.firstclub.membership.entity.Tier;
import com.firstclub.membership.exception.EntityNotFoundException;
import com.firstclub.membership.repository.TierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TierService {

    private final TierRepository tierRepository;

    @Transactional(readOnly = true)
    public List<TierResponse> getAllActiveTiers() {
        return tierRepository.findAllByActiveTrueOrderByRankAsc()
                .stream()
                .map(TierResponse::from)
                .toList();
    }

    public Tier requireLowestActiveTier() {
        return tierRepository.findFirstByActiveTrueOrderByRankAsc()
                .orElseThrow(() -> new EntityNotFoundException("No active tiers configured"));
    }
}
