package com.firstclub.membership.controller;

import com.firstclub.membership.dto.response.ExpiryResponse;
import com.firstclub.membership.dto.response.MembershipResponse;
import com.firstclub.membership.dto.response.TierResponse;
import com.firstclub.membership.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/{userId}/membership")
@RequiredArgsConstructor
public class MembershipInfoController {

    private final SubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<MembershipResponse> getMembership(@PathVariable Long userId) {
        return ResponseEntity.ok(subscriptionService.getActiveMembership(userId));
    }

    @GetMapping("/expiry")
    public ResponseEntity<ExpiryResponse> getExpiry(@PathVariable Long userId) {
        return ResponseEntity.ok(new ExpiryResponse(subscriptionService.getExpiryDate(userId)));
    }

    @GetMapping("/tier")
    public ResponseEntity<TierResponse> getCurrentTier(@PathVariable Long userId) {
        return ResponseEntity.ok(subscriptionService.getCurrentTier(userId));
    }
}
