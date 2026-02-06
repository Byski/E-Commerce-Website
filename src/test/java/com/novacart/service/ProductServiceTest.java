package com.novacart.service;

import com.novacart.dto.ProductRequest;
import com.novacart.dto.ProductResponse;
import com.novacart.model.Category;
import com.novacart.model.Product;
import com.novacart.repository.CategoryRepository;
import com.novacart.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

class ProductServiceTest {
    @Test
    void createProductCreatesCategoryWhenMissing() {
        ProductRepository productRepository = Mockito.mock(ProductRepository.class);
        CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);

        Mockito.when(categoryRepository.findByNameIgnoreCase("Books")).thenReturn(Optional.empty());
        Mockito.when(categoryRepository.save(any(Category.class))).thenAnswer(inv -> inv.getArgument(0));
        Mockito.when(productRepository.save(any(Product.class))).thenAnswer(inv -> {
            Product product = inv.getArgument(0);
            product.setActive(true);
            return product;
        });

        ProductService productService = new ProductService(productRepository, categoryRepository);

        ProductRequest request = new ProductRequest();
        setField(request, "name", "Book");
        setField(request, "description", "Paperback");
        setField(request, "price", new BigDecimal("10.00"));
        setField(request, "stock", 10);
        setField(request, "category", "Books");

        ProductResponse response = productService.createProduct(request);
        assertEquals("Book", response.getName());
        assertEquals("Books", response.getCategory());
    }

    private void setField(Object target, String field, Object value) {
        try {
            var f = target.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(target, value);
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }
}
