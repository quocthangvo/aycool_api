package com.example.shopapp_api.repositories.product;

import com.example.shopapp_api.entities.products.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByName(String name);

    boolean existsBySku(String sku);

    Page<Product> findAll(Pageable pageable);

    Optional<Product> findById(int productId);

    @Query("SELECT p FROM Product p WHERE " +
            "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:materialId IS NULL OR p.material.id = :materialId)")
    Page<Product> searchByNameAndMaterial(@Param("name") String name,
                                          @Param("materialId") Integer materialId,
                                          Pageable pageable);

}
