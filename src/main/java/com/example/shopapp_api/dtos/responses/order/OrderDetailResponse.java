package com.example.shopapp_api.dtos.responses.order;

import com.example.shopapp_api.entities.orders.OrderDetail;
import com.example.shopapp_api.entities.prices.Price;
import com.example.shopapp_api.entities.products.ProductImage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailResponse {

    private int id;

    @JsonProperty("order_id")
    private int orderId;

    @JsonProperty("product_detail_id")
    private int productDetailId;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("total_money")
    private Float totalMoney;


    @JsonProperty("product_name")
    private String productName;  // Thêm tên sản phẩm

    @JsonProperty("size")
    private String sizeName;     // Thêm size

    @JsonProperty("color")
    private String colorName;    // Thêm màu sắc

    private List<Price> price;

    @JsonProperty("image_url")
    private List<ProductImage> imageUrl;


    public static OrderDetailResponse formOrderDetail(OrderDetail orderDetail) {
        OrderDetailResponse orderDetailResponse = OrderDetailResponse
                .builder()
                .id(orderDetail.getId())
                .orderId(orderDetail.getOrder().getId())
                .productDetailId(orderDetail.getProductDetail().getId())
//                .price(orderDetail.getPrice())
                .quantity(orderDetail.getQuantity())
                .totalMoney(orderDetail.getTotalMoney())
                .productName(orderDetail.getProductDetail().getProduct().getName())
                .sizeName(orderDetail.getProductDetail().getSize().getName())
                .colorName(orderDetail.getProductDetail().getColor().getName())
                .price(orderDetail.getProductDetail().getPrices())
                .imageUrl(orderDetail.getProductDetail().getProduct().getProductImages())
                .build();
        return orderDetailResponse;
    }
}
