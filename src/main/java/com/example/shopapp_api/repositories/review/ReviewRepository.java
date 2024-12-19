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


    Page<Review> findByProductId(int productId, Pageable pageable);

    List<Review> findByProductId(int productId);

    boolean existsByOrderIdAndProductIdAndUserId(int orderId, int productId, int userId);

    @Query("SELECT SUM(r.rating) FROM Review r WHERE r.product.id = :productId")
    Long getTotalStarsByProductId(@Param("productId") int productId);

    boolean existsByOrderIdAndUserId(Long orderId, Long userId);
}
