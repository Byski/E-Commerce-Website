package com.novacart.dto;

import java.math.BigDecimal;
import java.util.List;

public class CartResponse {
    private final List<CartItemResponse> items;
    private final BigDecimal total;

    public CartResponse(List<CartItemResponse> items, BigDecimal total) {
        this.items = items;
        this.total = total;
    }

    public List<CartItemResponse> getItems() {
        return items;
    }

    public BigDecimal getTotal() {
        return total;
    }
}
