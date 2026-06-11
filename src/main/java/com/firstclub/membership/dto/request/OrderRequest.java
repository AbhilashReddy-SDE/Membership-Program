package com.firstclub.membership.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderRequest(
        @NotNull(message = "orderValue is required")
        @DecimalMin(value = "0.0", message = "orderValue must be >= 0")
        BigDecimal orderValue
) {}
