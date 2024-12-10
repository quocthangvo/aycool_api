package com.example.shopapp_api.dtos.responses.product.products;

import com.example.shopapp_api.dtos.responses.product.ProductDetailResponse;
import com.example.shopapp_api.entities.attributes.Color;
import com.example.shopapp_api.entities.attributes.Size;
import com.example.shopapp_api.entities.prices.Price;
import com.example.shopapp_api.entities.products.Product;
import com.example.shopapp_api.entities.products.ProductDetail;
import com.example.shopapp_api.entities.products.ProductImage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor//khởi tạo all
@NoArgsConstructor//khởi tạo mặc định k tham số
@Builder//dùng để trả về build khi cần dùng đến create hay update sẽ gọi dc các phần tữ phái dưới

public class ProductNameResponse {
    private int id;

    @JsonProperty("sku_version")
    private String skuVersion;

    @JsonProperty("product_id")
    private int productId;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("color_id")
    private Color colorId;

    @JsonProperty("size_id")
    private Size sizeId;

    @JsonProperty("image_url")
    private List<ProductImage> imageUrl;

    public static ProductNameResponse formProductName(ProductDetail productDetail) {

        ProductNameResponse productNameResponse = ProductNameResponse.builder()
                .id(productDetail.getId())
                .skuVersion(productDetail.getSkuVersion())
                .productId(productDetail.getProduct().getId())
                .productName(productDetail.getProduct().getName())
                .colorId(productDetail.getColor())
                .sizeId(productDetail.getSize())
                .imageUrl(productDetail.getProduct().getProductImages())
                .build();

        return productNameResponse;
    }
}
