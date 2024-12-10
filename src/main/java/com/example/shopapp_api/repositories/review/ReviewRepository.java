package com.example.shopapp_api.repositories.review;


import com.example.shopapp_api.entities.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {


    Page<Review> findByProductDetailId(int productDetailId, Pageable pageable);

    List<Review> findByProductDetailId(int productDetailId);

    //kt đã dánh giá chưa
    Optional<Review> findByUserIdAndProductDetailId(int userId, int productDetailId);

//    Optional<Review> findByUserIdAndOrderId(int userId, int orderId);

}
