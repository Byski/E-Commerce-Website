package com.novacart.service;

import com.novacart.dto.CartItemResponse;
import com.novacart.dto.CartResponse;
import com.novacart.exception.BadRequestException;
import com.novacart.exception.NotFoundException;
import com.novacart.model.Cart;
import com.novacart.model.CartItem;
import com.novacart.model.Product;
import com.novacart.model.User;
import com.novacart.repository.CartRepository;
import com.novacart.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public CartResponse getCart(User user) {
        Cart cart = getOrCreateCart(user);
        return toResponse(cart);
    }

    public CartResponse addItem(User user, Long productId, Integer quantity) {
        Cart cart = getOrCreateCart(user);
        Product product = getActiveProduct(productId);
        CartItem item = cart.getItems().stream()
                .filter(existing -> existing.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (item == null) {
            item = new CartItem(cart, product, quantity, product.getPrice());
            cart.getItems().add(item);
        } else {
            item.setQuantity(item.getQuantity() + quantity);
            item.setUnitPrice(product.getPrice());
        }

        cartRepository.save(cart);
        return toResponse(cart);
    }

    public CartResponse updateItem(User user, Long productId, Integer quantity) {
        Cart cart = getOrCreateCart(user);
        CartItem item = cart.getItems().stream()
                .filter(existing -> existing.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Item not found in cart"));

        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be at least 1");
        }

        Product product = getActiveProduct(productId);
        item.setQuantity(quantity);
        item.setUnitPrice(product.getPrice());
        cartRepository.save(cart);
        return toResponse(cart);
    }

    public CartResponse removeItem(User user, Long productId) {
        Cart cart = getOrCreateCart(user);
        boolean removed = cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        if (!removed) {
            throw new NotFoundException("Item not found in cart");
        }
        cartRepository.save(cart);
        return toResponse(cart);
    }

    public void clearCart(Cart cart) {
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user).orElseGet(() -> cartRepository.save(new Cart(user)));
    }

    private Product getActiveProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        if (!product.getActive()) {
            throw new BadRequestException("Product is inactive");
        }
        return product;
    }

    private CartResponse toResponse(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream()
                .map(item -> {
                    BigDecimal lineTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                    return new CartItemResponse(item.getProduct().getId(), item.getProduct().getName(),
                            item.getQuantity(), item.getUnitPrice(), lineTotal);
                })
                .toList();
        BigDecimal total = items.stream()
                .map(CartItemResponse::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new CartResponse(items, total);
    }
}
