package com.example.shopapp_api.services.Serv.review;

import com.example.shopapp_api.dtos.requests.review.ReviewDTO;
import com.example.shopapp_api.dtos.requests.review.ReviewRequest;
import com.example.shopapp_api.dtos.responses.review.ReviewResponse;
import com.example.shopapp_api.entities.orders.Order;
import com.example.shopapp_api.entities.orders.OrderDetail;
import com.example.shopapp_api.entities.orders.status.OrderStatus;
import com.example.shopapp_api.entities.products.Product;
import com.example.shopapp_api.entities.products.ProductDetail;
import com.example.shopapp_api.entities.review.Review;
import com.example.shopapp_api.entities.users.User;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.repositories.order.OrderDetailRepository;
import com.example.shopapp_api.repositories.order.OrderRepository;
import com.example.shopapp_api.repositories.product.ProductDetailRepository;
import com.example.shopapp_api.repositories.product.ProductRepository;
import com.example.shopapp_api.repositories.review.ReviewRepository;
import com.example.shopapp_api.repositories.user.UserRepository;
import com.example.shopapp_api.services.Impl.review.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@RestController
public class ReviewService implements IReviewService {
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductDetailRepository productDetailRepository;


    @Override
    public Page<ReviewResponse> getReviewsForProduct(int productId, int page, int limit) {
        // Tạo Pageable object với thông tin phân trang
        Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Order.desc("createdAt"))); // Có thể thêm trường sort theo thời gian nếu cần

        // Lấy các đánh giá từ repository chỉ với status = 1
        Page<Review> reviewsPage = reviewRepository
                .findByProductIdAndStatus(productId, true, pageable); // Chỉ lấy đánh giá có status = 1 (hiển thị)

        // Chuyển các Review thành ReviewResponse
        Page<ReviewResponse> reviewResponses = reviewsPage.map(review -> ReviewResponse.formReview(review));

        return reviewResponses;
    }


    @Override
    public Long getTotalStars(int productId) {
        return reviewRepository.getTotalStarsByProductId(productId);
    }


    @Override
    public void deleteReview(int reviewId) throws DataNotFoundException {
        // Tìm đánh giá theo reviewId
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);

        // Kiểm tra nếu không tìm thấy đánh giá
        if (!reviewOptional.isPresent()) {
            throw new DataNotFoundException("Không tìm thấy đánh giá với ID này");
        }

        // Xóa đánh giá
        reviewRepository.deleteById(reviewId);
    }

    //ẩn đánh giá
    // Tìm kiếm review theo reviewId
    @Override
    public Review findReviewById(int reviewId) throws DataNotFoundException {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đánh giá với ID này"));
    }

    // Lưu review sau khi cập nhật trạng thái
    @Override
    public void saveReview(Review review) {
        reviewRepository.save(review);
    }


    @Transactional
    @Override
    public Review createReview(ReviewDTO reviewDTO) {
        // Fetch the order and product details
        Order order = orderRepository.findById(reviewDTO.getOrderId()).orElseThrow(() -> new RuntimeException("Order not found"));
        Product product = productRepository.findById(reviewDTO.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));

        // Kiểm tra trạng thái của đơn hàng (phải là 'DELIVERED' mới được đánh giá)
        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new IllegalArgumentException("Đơn hàng phải được giao mới có thể đánh giá");
        }

        // Kiểm tra xem người dùng có phải là người đã đặt đơn hàng này không
        if (order.getUser().getId() != reviewDTO.getUserId()) {
            throw new IllegalArgumentException("Người dùng không có quyền đánh giá đơn hàng này");
        }

        // Kiểm tra xem người dùng đã đánh giá sản phẩm này trong đơn hàng chưa
        boolean alreadyReviewed = reviewRepository.existsByOrderIdAndProductIdAndUserId(
                reviewDTO.getOrderId(), reviewDTO.getProductId(), reviewDTO.getUserId()
        );
        if (alreadyReviewed) {
            throw new IllegalArgumentException("Bạn đã đánh giá sản phẩm này trong đơn hàng này");
        }

        // Create the review entity
        Review review = new Review();
        review.setUser(order.getUser());
        review.setOrder(order);
        review.setProduct(product);
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setCreatedAt(LocalDateTime.now());

        // Save the review
        return reviewRepository.save(review);
    }

    @Override
    public boolean hasReviewed(Long orderId, Long userId) {
        return reviewRepository.existsByOrderIdAndUserId(orderId, userId);
    }

    @Override
    public Page<ReviewResponse> getReviews(int productId, int page, int limit) {
        // Tạo Pageable object với thông tin phân trang
        Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Order.desc("createdAt"))); // Sắp xếp theo thời gian tạo

        // Lấy tất cả các đánh giá (bao gồm cả ẩn và hiện)
        Page<Review> reviewsPage = reviewRepository.findByProductId(productId, pageable);

        // Chuyển các Review thành ReviewResponse
        Page<ReviewResponse> reviewResponses = reviewsPage.map(review -> ReviewResponse.formReview(review));

        return reviewResponses;
    }


}
