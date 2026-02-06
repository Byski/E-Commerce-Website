package com.novacart.dto;

import java.math.BigDecimal;

public class CartItemResponse {
    private final Long productId;
    private final String name;
    private final Integer quantity;
    private final BigDecimal unitPrice;
    private final BigDecimal lineTotal;

    public CartItemResponse(Long productId, String name, Integer quantity, BigDecimal unitPrice, BigDecimal lineTotal) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineTotal = lineTotal;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }
}
