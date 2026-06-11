package com.firstclub.membership.controller;

import com.firstclub.membership.dto.response.PlanResponse;
import com.firstclub.membership.dto.response.TierResponse;
import com.firstclub.membership.service.MembershipPlanService;
import com.firstclub.membership.service.TierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/membership")
@RequiredArgsConstructor
public class MembershipCatalogController {

    private final MembershipPlanService planService;
    private final TierService tierService;

    @GetMapping("/plans")
    public ResponseEntity<List<PlanResponse>> listPlans() {
        return ResponseEntity.ok(planService.getAllActivePlans());
    }

    @GetMapping("/tiers")
    public ResponseEntity<List<TierResponse>> listTiers() {
        return ResponseEntity.ok(tierService.getAllActiveTiers());
    }

}
