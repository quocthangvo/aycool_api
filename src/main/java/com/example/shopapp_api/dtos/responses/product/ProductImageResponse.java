package com.example.shopapp_api.dtos.responses.product;

import com.example.shopapp_api.entities.products.Product;
import com.example.shopapp_api.entities.products.ProductImage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImageResponse {
    private int id;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("product_id")
    private int productId;

    public static ProductImageResponse formProductImage(ProductImage productImage) {
        ProductImageResponse productImageResponse = ProductImageResponse.builder()
                .id(productImage.getId())
                .imageUrl(productImage.getImageUrl())
                .productId(productImage.getProduct().getId())

                .build();

        return productImageResponse;
    }
}
