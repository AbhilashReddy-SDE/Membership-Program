package com.firstclub.membership.controller;

import com.firstclub.membership.dto.request.PlanChangeRequest;
import com.firstclub.membership.dto.request.SubscribeRequest;
import com.firstclub.membership.dto.response.MembershipResponse;
import com.firstclub.membership.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/{userId}/membership")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<MembershipResponse> subscribe(
            @PathVariable Long userId,
            @Valid @RequestBody SubscribeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subscriptionService.subscribe(userId, request.planCode()));
    }

    @PutMapping("/upgrade")
    public ResponseEntity<MembershipResponse> upgrade(
            @PathVariable Long userId,
            @Valid @RequestBody PlanChangeRequest request) {
        return ResponseEntity.ok(subscriptionService.upgrade(userId, request.planCode()));
    }

    @PutMapping("/downgrade")
    public ResponseEntity<MembershipResponse> downgrade(
            @PathVariable Long userId,
            @Valid @RequestBody PlanChangeRequest request) {
        return ResponseEntity.ok(subscriptionService.downgrade(userId, request.planCode()));
    }

    @DeleteMapping
    public ResponseEntity<MembershipResponse> cancel(@PathVariable Long userId) {
        return ResponseEntity.ok(subscriptionService.cancel(userId));
    }
}
