package com.novacart.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String providerRef;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    protected Payment() {
    }

    public Payment(Order order, PaymentStatus status, BigDecimal amount, String providerRef) {
        this.order = order;
        this.status = status;
        this.amount = amount;
        this.providerRef = providerRef;
    }

    public Long getId() {
        return id;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getProviderRef() {
        return providerRef;
    }
}
