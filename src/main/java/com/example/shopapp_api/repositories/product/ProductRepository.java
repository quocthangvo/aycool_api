package com.example.shopapp_api.repositories.product;

import com.example.shopapp_api.entities.products.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByName(String name);

    boolean existsBySku(String sku);

    Page<Product> findAll(Pageable pageable);

    Optional<Product> findById(int productId);
}
