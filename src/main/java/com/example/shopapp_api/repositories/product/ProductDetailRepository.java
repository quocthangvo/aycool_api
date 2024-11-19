package com.example.shopapp_api.repositories.product;

import com.example.shopapp_api.entities.attributes.Color;
import com.example.shopapp_api.entities.attributes.Size;
import com.example.shopapp_api.entities.products.Product;
import com.example.shopapp_api.entities.products.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {
    List<ProductDetail> findByProductId(int productId);

    Optional<ProductDetail> findByProductAndColorAndSize(Product product, Color color, Size size);

    boolean existsBySkuVersion(String skuVersion);

    boolean existsByProductId(int productId);

    List<ProductDetail> findAllByOrderByIdDesc();

}
