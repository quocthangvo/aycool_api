package com.example.shopapp_api.repositories.price;

import com.example.shopapp_api.entities.prices.Price;
import com.example.shopapp_api.entities.products.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceRepository extends JpaRepository<Price, Integer> {
    List<Price> findByProductDetailId(int productDetailId);

    Price findTopByProductDetailIdOrderByCreatedAtDesc(int productDetailId);
}
