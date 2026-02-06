package com.novacart.service;

import com.novacart.dto.ProductRequest;
import com.novacart.dto.ProductResponse;
import com.novacart.exception.NotFoundException;
import com.novacart.model.Category;
import com.novacart.model.Product;
import com.novacart.repository.CategoryRepository;
import com.novacart.repository.ProductRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<ProductResponse> listProducts(String query, String category, BigDecimal minPrice,
                                              BigDecimal maxPrice, boolean includeInactive) {
        Specification<Product> spec = buildSpec(query, category, minPrice, maxPrice, includeInactive);
        return productRepository.findAll(spec).stream()
                .map(this::toResponse)
                .toList();
    }

    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .filter(Product::getActive)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        return toResponse(product);
    }

    public ProductResponse createProduct(ProductRequest request) {
        Category category = resolveCategory(request.getCategory());
        Product product = new Product(request.getName(), request.getDescription(), request.getPrice(),
                request.getStock(), category);
        return toResponse(productRepository.save(product));
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(resolveCategory(request.getCategory()));
        return toResponse(productRepository.save(product));
    }

    public ProductResponse deactivateProduct(Long id, boolean active) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        product.setActive(active);
        return toResponse(productRepository.save(product));
    }

    private Category resolveCategory(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        return categoryRepository.findByNameIgnoreCase(name)
                .orElseGet(() -> categoryRepository.save(new Category(name)));
    }

    private ProductResponse toResponse(Product product) {
        String category = product.getCategory() != null ? product.getCategory().getName() : null;
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(),
                product.getPrice(), product.getStock(), product.getActive(), category);
    }

    private Specification<Product> buildSpec(String query, String category, BigDecimal minPrice,
                                             BigDecimal maxPrice, boolean includeInactive) {
        return (root, queryBuilder, criteria) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (query != null && !query.isBlank()) {
                String like = "%" + query.toLowerCase() + "%";
                predicates.add(criteria.or(
                        criteria.like(criteria.lower(root.get("name")), like),
                        criteria.like(criteria.lower(root.get("description")), like)
                ));
            }
            if (category != null && !category.isBlank()) {
                predicates.add(criteria.equal(criteria.lower(root.join("category").get("name")),
                        category.toLowerCase()));
            }
            if (minPrice != null) {
                predicates.add(criteria.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(criteria.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
            if (!includeInactive) {
                predicates.add(criteria.isTrue(root.get("active")));
            }
            return criteria.and(predicates.toArray(new Predicate[0]));
        };
    }
}
