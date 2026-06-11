package com.firstclub.membership.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SubscribeRequest(
        @NotBlank(message = "planCode is required") String planCode
) {}
