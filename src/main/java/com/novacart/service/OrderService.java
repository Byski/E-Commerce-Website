package com.novacart.service;

import com.novacart.dto.OrderItemResponse;
import com.novacart.dto.OrderResponse;
import com.novacart.exception.NotFoundException;
import com.novacart.model.Order;
import com.novacart.model.User;
import com.novacart.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<OrderResponse> listOrders(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(this::toResponse)
                .toList();
    }

    public OrderResponse getOrder(User user, Long orderId) {
        Order order = orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        return toResponse(order);
    }

    public OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(item -> {
                    BigDecimal lineTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                    return new OrderItemResponse(item.getProduct().getId(), item.getProduct().getName(),
                            item.getQuantity(), item.getUnitPrice(), lineTotal);
                })
                .toList();
        return new OrderResponse(order.getId(), order.getStatus(),
                order.getPayment() != null ? order.getPayment().getStatus() : null,
                order.getTotalAmount(), order.getCreatedAt(), items);
    }
}
