package com.example.shopapp_api.controllers.review;

import com.example.shopapp_api.dtos.requests.review.ReviewDTO;
import com.example.shopapp_api.dtos.responses.apiResponse.ApiResponse;
import com.example.shopapp_api.dtos.responses.apiResponse.MessageResponse;
import com.example.shopapp_api.dtos.responses.review.ReviewListResponse;
import com.example.shopapp_api.dtos.responses.review.ReviewResponse;
import com.example.shopapp_api.entities.review.Review;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.services.Impl.review.IReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final IReviewService reviewService;


    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<ReviewListResponse>> getReviewsForProductDetail(
            @PathVariable int productId,
            @RequestParam int page,
            @RequestParam int limit) {
        try {
            // Gọi service để lấy danh sách đánh giá phân trang
            Page<ReviewResponse> reviewsPage = reviewService.getReviewsForProduct(productId, page, limit);

            // Lấy tổng số sao từ service
            Long totalStars = reviewService.getTotalStars(productId);

            int totalPages = reviewsPage.getTotalPages();//lấy ra tổng số trang
            long totalRecords = reviewsPage.getTotalElements();
            List<ReviewResponse> reviewList = reviewsPage.getContent();//từ productPgae lấy ra ds các product getContent

            ReviewListResponse reviewListResponse = (ReviewListResponse
                    .builder()
                    .reviewResponseList(reviewList)
                    .totalPages(totalPages)
                    .totalRecords(totalRecords)
                    .totalStars(totalStars != null ? totalStars : 0)
                    .build());
            return ResponseEntity.ok(new ApiResponse<>("Thành công", reviewListResponse));

        } catch (Exception e) {
            // Xử lý lỗi
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Đã xảy ra lỗi khi lấy danh sách đánh giá", null));
        }
    }


    @DeleteMapping("/delete/product/{productDetailId}")
    public ResponseEntity<MessageResponse> deleteReview(@PathVariable int productDetailId) {
        try {
            // Gọi service để xóa các đánh giá của sản phẩm
            reviewService.deleteReview(productDetailId);

            // Trả về phản hồi thành công nếu không có ngoại lệ
            return ResponseEntity.ok(new MessageResponse("Xóa đánh giá thành công"));

        } catch (DataNotFoundException e) {
            // Xử lý trường hợp không tìm thấy đánh giá
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Không tìm thấy đánh giá cho sản phẩm này"));
        } catch (Exception e) {
            // Xử lý các ngoại lệ khác
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Đã xảy ra lỗi trong quá trình xóa đánh giá"));
        }
    }


    @PostMapping("")
    public ResponseEntity<ApiResponse<?>> createReview(@RequestBody ReviewDTO reviewDTO) {
        try {
            Review review = reviewService.createReview(
                    reviewDTO
            );
            return ResponseEntity.ok(new ApiResponse<>("Thành công", review));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @GetMapping("/hasReviewed/{orderId}/{userId}")
    public boolean hasReviewed(@PathVariable Long orderId, @PathVariable Long userId) {
        return reviewService.hasReviewed(orderId, userId);
    }
}
