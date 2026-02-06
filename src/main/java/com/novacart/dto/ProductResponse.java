package com.novacart.dto;

import java.math.BigDecimal;

public class ProductResponse {
    private final Long id;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final Integer stock;
    private final Boolean active;
    private final String category;

    public ProductResponse(Long id, String name, String description, BigDecimal price, Integer stock, Boolean active, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.active = active;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }

    public Boolean getActive() {
        return active;
    }

    public String getCategory() {
        return category;
    }
}
