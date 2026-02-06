package com.novacart.controller;

import com.novacart.dto.CheckoutRequest;
import com.novacart.dto.OrderResponse;
import com.novacart.model.User;
import com.novacart.service.CheckoutService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> checkout(@AuthenticationPrincipal User user,
                                                  @Valid @RequestBody CheckoutRequest request) {
        return ResponseEntity.ok(checkoutService.checkout(user, request.getSimulateSuccess()));
    }
}
