package com.novacart.service;

import com.novacart.dto.OrderResponse;
import com.novacart.exception.BadRequestException;
import com.novacart.model.*;
import com.novacart.repository.OrderRepository;
import com.novacart.repository.PaymentRepository;
import com.novacart.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class CheckoutService {
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;
    private final OrderService orderService;

    public CheckoutService(CartService cartService, OrderRepository orderRepository, PaymentRepository paymentRepository,
                           ProductRepository productRepository, OrderService orderService) {
        this.cartService = cartService;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.productRepository = productRepository;
        this.orderService = orderService;
    }

    @Transactional
    public OrderResponse checkout(User user, boolean simulateSuccess) {
        Cart cart = cartService.getOrCreateCart(user);
        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        cart.getItems().forEach(item -> {
            if (item.getProduct().getStock() < item.getQuantity()) {
                throw new BadRequestException("Insufficient stock for " + item.getProduct().getName());
            }
        });

        BigDecimal total = cart.getItems().stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order(user, OrderStatus.PENDING, total);
        cart.getItems().forEach(item -> order.getItems()
                .add(new OrderItem(order, item.getProduct(), item.getQuantity(), item.getUnitPrice())));
        orderRepository.save(order);

        PaymentStatus paymentStatus = simulateSuccess ? PaymentStatus.APPROVED : PaymentStatus.DECLINED;
        Payment payment = new Payment(order, paymentStatus, total, "SIM-" + UUID.randomUUID());
        paymentRepository.save(payment);
        order.setPayment(payment);

        if (paymentStatus == PaymentStatus.APPROVED) {
            order.setStatus(OrderStatus.PAID);
            cart.getItems().forEach(item -> {
                Product product = item.getProduct();
                product.setStock(product.getStock() - item.getQuantity());
                productRepository.save(product);
            });
            cartService.clearCart(cart);
        } else {
            order.setStatus(OrderStatus.FAILED);
        }

        return orderService.toResponse(order);
    }
}
