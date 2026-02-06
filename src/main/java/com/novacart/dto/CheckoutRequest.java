package com.novacart.dto;

import jakarta.validation.constraints.NotNull;

public class CheckoutRequest {
    @NotNull
    private Boolean simulateSuccess;

    public Boolean getSimulateSuccess() {
        return simulateSuccess;
    }
}
