package com.example.shopapp_api.services.Serv.review;

import com.example.shopapp_api.dtos.requests.review.ReviewDTO;
import com.example.shopapp_api.dtos.responses.review.ReviewResponse;
import com.example.shopapp_api.entities.orders.Order;
import com.example.shopapp_api.entities.orders.OrderDetail;
import com.example.shopapp_api.entities.orders.OrderStatus;
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
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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


//    @Override
//    public Review createReview(int userId, int productDetailId, ReviewDTO reviewDTO) {
//        // Kiểm tra người dùng có tồn tại không
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
//
//        // Kiểm tra sản phẩm chi tiết có tồn tại không
//        ProductDetail productDetail = productDetailRepository.findById(productDetailId)
//                .orElseThrow(() -> new RuntimeException("Sản phẩm chi tiết không tồn tại"));
//
//
//        // Kiểm tra người dùng có đơn hàng đã giao và có sản phẩm này trong đơn hàng
//        Optional<Order> order = orderRepository
//                .findByUserIdAndStatusAndOrderDetailsProductDetailId(userId, OrderStatus.DELIVERED, productDetailId);
//
//        if (order.isEmpty()) {
//            throw new RuntimeException("Người dùng không có đơn hàng đã giao chứa sản phẩm này");
//        }
//
//        // Kiểm tra xem người dùng đã đánh giá sản phẩm này chưa
//        Optional<Review> existingReview = reviewRepository.findByUserIdAndProductDetailId(userId, productDetailId);
//        if (existingReview.isPresent()) {
//            throw new RuntimeException("Bạn đã đánh giá sản phẩm này rồi. Bạn chỉ có thể đánh giá lại khi mua lại sản phẩm.");
//        }
//        // Tạo mới review
//        Review review = new Review();
//        review.setUser(user);
//        review.setProductDetail(productDetail);
//        review.setRating(reviewDTO.getRating());
//        review.setComment(reviewDTO.getComment());
//        review.setOrder(order.get());
//
//        return reviewRepository.save(review);
//    }

    @Override
    public Page<ReviewResponse> getReviewsForProductDetail(int productDetailId, int page, int limit) {
        // Tạo Pageable object với thông tin phân trang
        Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Order.desc("createdAt"))); // Có thể thêm trường sort theo thời gian nếu cần

        // Lấy các đánh giá từ repository
        Page<Review> reviewsPage = reviewRepository.findByProductDetailId(productDetailId, pageable);

        // Chuyển các Review thành ReviewResponse
        Page<ReviewResponse> reviewResponses = reviewsPage.map(review -> ReviewResponse.formReview(review));

        return reviewResponses;
    }


    @Override
    public Review getReviewById(int id) throws DataNotFoundException {
        return null;
    }

    @Override
    public Page<Review> getAllReviews(PageRequest pageRequest) {
        return null;
    }

    @Override
    public void deleteReview(int productDetailId) throws DataNotFoundException {
        // Tìm tất cả đánh giá của sản phẩm với productDetailId
        List<Review> reviews = reviewRepository.findByProductDetailId(productDetailId);

        if (reviews.isEmpty()) {
            throw new DataNotFoundException("Không tìm thấy đánh giá nào cho sản phẩm này");
        }

        // Xóa tất cả đánh giá của sản phẩm này
        reviewRepository.deleteAll(reviews);
    }

    @Override
    public Review updateReview(int id, ReviewDTO reviewDTO) throws DataNotFoundException {
        return null;
    }

    @Override
    public Review createReview(int userId, int orderId, int productDetailId, ReviewDTO reviewDTO) {
        // Kiểm tra người dùng có tồn tại không
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        // Kiểm tra đơn hàng có tồn tại không và đã giao
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));

        if (!order.getStatus().equals(OrderStatus.DELIVERED)) {
            throw new RuntimeException("Đơn hàng chưa được giao");
        }

        // Kiểm tra sản phẩm có trong đơn hàng hay không
        Optional<OrderDetail> orderDetail = order.getOrderDetails().stream()
                .filter(detail -> detail.getProductDetail().getId() == productDetailId)
                .findFirst();

        if (orderDetail.isEmpty()) {
            throw new RuntimeException("Sản phẩm không có trong đơn hàng này");
        }

        // Kiểm tra người dùng đã đánh giá sản phẩm này chưa
        Optional<Review> existingReview = reviewRepository.findByUserIdAndProductDetailId(userId, productDetailId);
        if (existingReview.isPresent()) {
            throw new RuntimeException("Bạn đã đánh giá sản phẩm này rồi. Bạn chỉ có thể đánh giá lại khi mua lại sản phẩm.");
        }

        // Tạo mới review
        Review review = new Review();
        review.setUser(user);
        review.setProductDetail(orderDetail.get().getProductDetail());
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setOrder(order);
        review.setCreatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }


//    @Override
//    public List<Review> getReviewsByProductDetailAndOrder(int productDetailId, int orderId) {
//        return reviewRepository.findByProductDetailIdAndOrderId(productDetailId, orderId);
//    }
//
//    @Override
//    public List<ReviewResponse> getReviewsByProduct(int productDetailId) {
//        List<Review> reviews = reviewRepository.findReviewsByProductDetailId(productDetailId);
//        // Chuyển đổi dữ liệu sang DTO để trả về
//        return reviews.stream()
//                .map(ReviewResponse::formReview) // Sử dụng phương thức tĩnh để chuyển đổi
//                .collect(Collectors.toList());
//    }


}
