package com.novacart.controller;

import com.novacart.dto.CartItemRequest;
import com.novacart.dto.CartResponse;
import com.novacart.dto.UpdateCartItemRequest;
import com.novacart.model.User;
import com.novacart.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cartService.getCart(user));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(@AuthenticationPrincipal User user,
                                                @Valid @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.addItem(user, request.getProductId(), request.getQuantity()));
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<CartResponse> updateItem(@AuthenticationPrincipal User user,
                                                   @PathVariable Long productId,
                                                   @Valid @RequestBody UpdateCartItemRequest request) {
        return ResponseEntity.ok(cartService.updateItem(user, productId, request.getQuantity()));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartResponse> removeItem(@AuthenticationPrincipal User user,
                                                   @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeItem(user, productId));
    }
}
