package com.novacart.dto;

import com.novacart.model.OrderStatus;
import com.novacart.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class OrderResponse {
    private final Long id;
    private final OrderStatus status;
    private final PaymentStatus paymentStatus;
    private final BigDecimal totalAmount;
    private final Instant createdAt;
    private final List<OrderItemResponse> items;

    public OrderResponse(Long id, OrderStatus status, PaymentStatus paymentStatus, BigDecimal totalAmount,
                         Instant createdAt, List<OrderItemResponse> items) {
        this.id = id;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }
}
