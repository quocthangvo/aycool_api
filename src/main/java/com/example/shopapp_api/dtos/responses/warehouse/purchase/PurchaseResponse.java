package com.example.shopapp_api.dtos.responses.warehouse.purchase;

import com.example.shopapp_api.dtos.responses.product.ProductDetailResponse;
import com.example.shopapp_api.dtos.responses.warehouse.WarehouseResponse;
import com.example.shopapp_api.entities.categories.SubCategory;
import com.example.shopapp_api.entities.products.ProductDetail;
import com.example.shopapp_api.entities.products.ProductImage;
import com.example.shopapp_api.entities.warehouse.Purchase;
import com.example.shopapp_api.entities.warehouse.Warehouse;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor//khởi tạo all
@NoArgsConstructor//khởi tạo mặc định k tham số
@Builder//dùng để trả về build khi cần dùng đến create hay update sẽ gọi dc các phần tữ phái dưới

public class PurchaseResponse {

    private int id;

    private int quantity;


    private Float price;

    @JsonProperty("product_detail")
    private ProductDetail productDetail;

    private Warehouse warehouse;

    private LocalDateTime dateTime;

    @JsonProperty("image_url")
    private List<ProductImage> imageUrl;

    @JsonProperty("sub-category")
    private SubCategory subCategory;

    public static PurchaseResponse formPurchase(Purchase purchase) {
        PurchaseResponse purchaseResponse = PurchaseResponse.builder()
                .id(purchase.getId())
                .quantity(purchase.getQuantity())
                .price(purchase.getPrice())
                .productDetail(purchase.getProductDetail())
                .warehouse(purchase.getWarehouse())
                .dateTime(purchase.getDateTime())
                .imageUrl(purchase.getProductDetail().getProduct().getProductImages())
                .subCategory(purchase.getProductDetail().getProduct().getSubCategory())
                .build();

        return purchaseResponse;
    }
}
