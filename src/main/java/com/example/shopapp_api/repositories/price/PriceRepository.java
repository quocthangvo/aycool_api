package com.example.shopapp_api.repositories.price;

import com.example.shopapp_api.entities.prices.Price;
import com.example.shopapp_api.entities.products.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PriceRepository extends JpaRepository<Price, Integer> {
    List<Price> findByProductDetailId(int productDetailId);

//    Price findTopByProductDetailIdOrderByCreatedAtDesc(int productDetailId);

    //giá tiền theo id
    Optional<Price> findTopByProductDetailIdOrderByCreatedAtDesc(int productDetailId);

    List<Price> findAllByOrderByCreatedAtDesc();

    Price findFirstByProductDetailIdAndEndDateIsNull(int productDetailId);
}
