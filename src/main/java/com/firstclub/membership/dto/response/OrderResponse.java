package com.firstclub.membership.dto.response;

import com.firstclub.membership.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
        Long id,
        Long userId,
        BigDecimal orderValue,
        LocalDateTime orderDate
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getUser().getId(),
                order.getOrderValue(),
                order.getOrderDate()
        );
    }
}
