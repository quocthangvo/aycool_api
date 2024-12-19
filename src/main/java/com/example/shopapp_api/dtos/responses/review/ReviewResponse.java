package com.example.shopapp_api.dtos.responses.review;

import com.example.shopapp_api.dtos.responses.BaseResponse;
import com.example.shopapp_api.dtos.responses.product.ProductDetailResponse;
import com.example.shopapp_api.dtos.responses.product.products.ProductNameResponse;
import com.example.shopapp_api.entities.orders.Order;
import com.example.shopapp_api.entities.orders.OrderDetail;
import com.example.shopapp_api.entities.products.Product;
import com.example.shopapp_api.entities.products.ProductDetail;
import com.example.shopapp_api.entities.review.Review;
import com.example.shopapp_api.entities.users.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor//khởi tạo all
@NoArgsConstructor//khởi tạo mặc định k tham số
@Builder//dùng để trả về build khi cần dùng đến create hay update sẽ gọi dc các phần tữ phái dưới

public class ReviewResponse extends BaseResponse {
    private int id;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("rating")
    private int rating;

    @JsonProperty("product_id")
    private int productId;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("order_details")
    private List<OrderDetail> orderDetails;

    @JsonProperty("user_id")
    private int userId;

    private String name;

    @JsonProperty("order_id")
    private int orderId;

    public static ReviewResponse formReview(Review review) {

        ReviewResponse reviewResponse = ReviewResponse.builder()
                .id(review.getId())
                .comment(review.getComment())
                .rating(review.getRating())
                .productId(review.getProduct().getId())
                .productName(review.getProduct().getName())
                .orderDetails(review.getOrder().getOrderDetails())
                .userId(review.getUser().getId())
                .name(review.getUser().getFullName())
                .orderId(review.getOrder().getId())
                .build();
        reviewResponse.setCreatedAt(review.getCreatedAt());
        reviewResponse.setUpdatedAt(review.getUpdatedAt());
        return reviewResponse;
    }

}
