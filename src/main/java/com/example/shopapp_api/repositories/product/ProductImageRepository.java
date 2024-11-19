package com.example.shopapp_api.repositories.product;

import com.example.shopapp_api.entities.products.Product;
import com.example.shopapp_api.entities.products.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    List<ProductImage> findByProductId(int productId);

    List<ProductImage> findByProduct(Product product);
}
