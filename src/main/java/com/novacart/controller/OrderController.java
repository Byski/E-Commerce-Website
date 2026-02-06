package com.novacart.controller;

import com.novacart.dto.OrderResponse;
import com.novacart.model.User;
import com.novacart.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> listOrders(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.listOrders(user));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@AuthenticationPrincipal User user, @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrder(user, orderId));
    }
}
