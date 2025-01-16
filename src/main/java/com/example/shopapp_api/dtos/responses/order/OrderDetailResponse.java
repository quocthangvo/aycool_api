package com.example.shopapp_api.dtos.responses.order;

import com.example.shopapp_api.entities.orders.OrderDetail;
import com.example.shopapp_api.entities.prices.Price;
import com.example.shopapp_api.entities.products.ProductDetail;
import com.example.shopapp_api.entities.products.ProductImage;
import com.example.shopapp_api.entities.warehouse.Warehouse;
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

    @JsonProperty("product_id")
    private int productId;

    @JsonProperty("product_name")
    private String productName;  // Thêm tên sản phẩm

    @JsonProperty("size")
    private String sizeName;     // Thêm size

    @JsonProperty("color")
    private String colorName;    // Thêm màu sắc

    private List<Price> price;

    @JsonProperty("image_url")
    private List<ProductImage> imageUrl;

    @JsonProperty("quantity_sold")
    private int quantitySold;

    public static int calculateTotalProductInWarehouse(List<ProductDetail> productDetails) {
        return productDetails.stream()
                .flatMap(productDetail -> productDetail.getWarehouses().stream())  // Duyệt qua từng kho hàng của mỗi chi tiết sản phẩm
                .mapToInt(Warehouse::getSellQuantity)  // Lấy số lượng bán từ mỗi kho
                .sum();  // Tổng hợp tất cả số lượng bán từ các kho
    }


    public static OrderDetailResponse formOrderDetail(OrderDetail orderDetail) {
        List<ProductDetail> productDetails = orderDetail.getProductDetail().getProduct().getProductDetails();  // Lấy danh sách chi tiết sản phẩm từ order
        int totalProductInWarehouse = calculateTotalProductInWarehouse(productDetails);  // Tính tổng số lượng sản phẩm trong kho
        OrderDetailResponse orderDetailResponse = OrderDetailResponse
                .builder()
                .id(orderDetail.getId())
                .orderId(orderDetail.getOrder().getId())
                .productDetailId(orderDetail.getProductDetail().getId())
                .quantity(orderDetail.getQuantity())
                .totalMoney(orderDetail.getTotalMoney())
                .productId(orderDetail.getProductDetail().getProduct().getId())
                .productName(orderDetail.getProductDetail().getProduct().getName())
                .sizeName(orderDetail.getProductDetail().getSize().getName())
                .colorName(orderDetail.getProductDetail().getColor().getName())
                .price(orderDetail.getProductDetail().getPrices())
                .imageUrl(orderDetail.getProductDetail().getProduct().getProductImages())
                .quantitySold(totalProductInWarehouse)
                .build();
        return orderDetailResponse;
    }
}
