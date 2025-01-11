package com.example.shopapp_api.dtos.responses.warehouse;

import com.example.shopapp_api.dtos.responses.BaseResponse;

import com.example.shopapp_api.dtos.responses.product.ProductDetailResponse;
import com.example.shopapp_api.entities.products.Product;
import com.example.shopapp_api.entities.products.ProductDetail;
import com.example.shopapp_api.entities.warehouse.Purchase;
import com.example.shopapp_api.entities.warehouse.Warehouse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor//khởi tạo all
@NoArgsConstructor//khởi tạo mặc định k tham số
@Builder//dùng để trả về build khi cần dùng đến create hay update sẽ gọi dc các phần tữ phái dưới

public class WarehouseResponse extends BaseResponse {
    private int quantity;
    private Float price;

    @JsonProperty("product_detail")
    private ProductDetailResponse productDetailResponses;

    private Product productId;

    @JsonProperty("sell_quantity")
    private int sellQuantity;

    @JsonProperty("remaining_quantity")
    private int remainingQuantity;

    public static WarehouseResponse formWarehouse(Warehouse warehouse) {
        WarehouseResponse warehouseResponse = WarehouseResponse.builder()
                .quantity(warehouse.getQuantity())
                .price(warehouse.getPrice())
                .productDetailResponses(ProductDetailResponse.formProductDetail(warehouse.getProductDetail()))
                .productId(warehouse.getProduct())
                .sellQuantity(warehouse.getSellQuantity())
                .remainingQuantity(warehouse.getRemainingQuantity())
                .build();
        warehouseResponse.setCreatedAt(warehouse.getCreatedAt());
        warehouseResponse.setUpdatedAt(warehouse.getUpdatedAt());
        return warehouseResponse;
    }


}
