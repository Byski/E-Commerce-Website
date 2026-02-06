package com.novacart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class UpdateCartItemRequest {
    @NotNull
    @Min(1)
    private Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }
}
