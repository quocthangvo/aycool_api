package com.example.shopapp_api.services.Impl.review;

import com.example.shopapp_api.dtos.requests.review.ReviewDTO;

import com.example.shopapp_api.dtos.requests.review.ReviewRequest;
import com.example.shopapp_api.dtos.responses.review.ReviewResponse;
import com.example.shopapp_api.entities.products.Product;
import com.example.shopapp_api.entities.products.ProductDetail;
import com.example.shopapp_api.entities.review.Review;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IReviewService {

    Page<ReviewResponse> getReviewsForProduct(int productId, int page, int limit);


    void deleteReview(int id) throws DataNotFoundException;


    Review createReview(ReviewDTO reviewDTO);

    Long getTotalStars(int productId);

    boolean hasReviewed(Long orderId, Long userId);
}
