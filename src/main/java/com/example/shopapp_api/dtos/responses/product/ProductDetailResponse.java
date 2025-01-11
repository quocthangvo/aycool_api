package com.example.shopapp_api.dtos.responses.product;

import com.example.shopapp_api.entities.attributes.Color;
import com.example.shopapp_api.entities.attributes.Size;
import com.example.shopapp_api.entities.prices.Price;
import com.example.shopapp_api.entities.products.Product;
import com.example.shopapp_api.entities.products.ProductDetail;
import com.example.shopapp_api.entities.products.ProductImage;
import com.example.shopapp_api.entities.warehouse.Warehouse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor//khởi tạo all
@NoArgsConstructor//khởi tạo mặc định k tham số
@Builder//dùng để trả về build khi cần dùng đến create hay update sẽ gọi dc các phần tữ phái dưới

public class ProductDetailResponse {
    private int id;

    @JsonProperty("sku_version")
    private String skuVersion;

    @JsonProperty("product_id")
    private Product productId;

    @JsonProperty("color_id")
    private Color colorId;

    @JsonProperty("size_id")
    private Size sizeId;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("price_id")
    private List<Price> priceId;

    @JsonProperty("sku_name")
    private String skuName;

    @JsonProperty("warehouse_quantity")
    private int warehouseQuantity; // Số lượng trong kho

//    @JsonProperty("image_url")
//    private List<ProductImage> imageUrl;


    public static ProductDetailResponse formProductDetail(ProductDetail productDetail) {

//        // Tính tổng số lượng từ tất cả các Warehouse liên kết với ProductDetail
//        int totalQuantity = productDetail.getWarehouses().stream()
//                .mapToInt(Warehouse::getQuantity)
//                .sum();

        ProductDetailResponse productDetailResponse = ProductDetailResponse.builder()
                .id(productDetail.getId())
                .skuVersion(productDetail.getSkuVersion())
                .productId(productDetail.getProduct())
                .colorId(productDetail.getColor())
                .sizeId(productDetail.getSize())
                .quantity(productDetail.getQuantity())
                .priceId(productDetail.getPrices())
                .skuName(productDetail.getSkuName())
//                .warehouseQuantity(totalQuantity)
//                .imageUrl(productDetail.getProduct().getProductImages())
                .build();

        return productDetailResponse;
    }

}
