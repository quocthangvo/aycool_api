package com.example.shopapp_api.dtos.responses.order;

import com.example.shopapp_api.entities.orders.OrderDetail;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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

    @JsonProperty("price")
    private Float price;

    @JsonProperty("product_name")
    private String productName;  // Thêm tên sản phẩm

    @JsonProperty("size")
    private String sizeName;     // Thêm size

    @JsonProperty("color")
    private String colorName;    // Thêm màu sắc

    public static OrderDetailResponse formOrderDetail(OrderDetail orderDetail) {
        OrderDetailResponse orderDetailResponse = OrderDetailResponse
                .builder()
                .id(orderDetail.getId())
                .orderId(orderDetail.getOrder().getId())
                .productDetailId(orderDetail.getProductDetail().getId())
                .price(orderDetail.getPrice())
                .totalMoney(orderDetail.getTotalMoney())
                .productName(orderDetail.getProductDetail().getProduct().getName())
                .sizeName(orderDetail.getProductDetail().getSize().getName())
                .colorName(orderDetail.getProductDetail().getColor().getName())
                .build();
        return orderDetailResponse;
    }
}
