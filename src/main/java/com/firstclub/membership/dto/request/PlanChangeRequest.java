package com.firstclub.membership.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PlanChangeRequest(
        @NotBlank(message = "planCode is required") String planCode
) {}
