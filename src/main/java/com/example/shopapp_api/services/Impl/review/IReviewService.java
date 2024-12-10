package com.example.shopapp_api.services.Impl.review;

import com.example.shopapp_api.dtos.requests.review.ReviewDTO;

import com.example.shopapp_api.dtos.responses.review.ReviewResponse;
import com.example.shopapp_api.entities.review.Review;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IReviewService {
//    Review createReview(int userId, int productDetailId, ReviewDTO reviewDTO);

    Page<ReviewResponse> getReviewsForProductDetail(int productDetailId, int page, int limit);

    Review getReviewById(int id) throws DataNotFoundException;

    Page<Review> getAllReviews(PageRequest pageRequest);

    void deleteReview(int id) throws DataNotFoundException;

    Review updateReview(int id, ReviewDTO reviewDTO) throws DataNotFoundException;

    Review createReview(int userId, int orderId, int productDetailId, ReviewDTO reviewDTO);

//    List<ReviewResponse> getReviewsForProductByOrder(int productDetailId);
}
