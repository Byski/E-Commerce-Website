package com.novacart.config;

import com.novacart.model.Category;
import com.novacart.model.Product;
import com.novacart.model.Role;
import com.novacart.model.User;
import com.novacart.repository.CategoryRepository;
import com.novacart.repository.ProductRepository;
import com.novacart.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder,
                               CategoryRepository categoryRepository, ProductRepository productRepository) {
        return args -> {
            if (!userRepository.existsByEmail("admin@novacart.local")) {
                User admin = new User("admin@novacart.local", passwordEncoder.encode("admin123"), Role.ADMIN);
                userRepository.save(admin);
            }

            if (productRepository.count() == 0) {
                Category electronics = categoryRepository.save(new Category("Electronics"));
                Category apparel = categoryRepository.save(new Category("Apparel"));

                productRepository.save(new Product("Nova Phone", "Smartphone with OLED display",
                        new BigDecimal("699.00"), 25, electronics));
                productRepository.save(new Product("Nova Headphones", "Wireless noise canceling headphones",
                        new BigDecimal("199.00"), 40, electronics));
                productRepository.save(new Product("Nova Hoodie", "Cotton hoodie with logo",
                        new BigDecimal("59.00"), 60, apparel));
            }
        };
    }
}
