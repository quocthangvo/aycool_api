package com.example.shopapp_api.dtos.responses.cart;


import com.example.shopapp_api.entities.cart.CartItem;
import com.example.shopapp_api.entities.products.ProductDetail;
import com.example.shopapp_api.entities.products.ProductImage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemResponse {
    private int id;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("product_detail_id")
    private ProductDetail productDetailResponse;

    private int quantity;

    @JsonProperty("image_url")
    private List<ProductImage> productImageResponse;

    @JsonProperty("product_name")
    private String productName;

    public static CartItemResponse formCartItem(CartItem cartItem) {
        CartItemResponse cartItemResponse = CartItemResponse.builder()
                .id(cartItem.getId())
                .userId(cartItem.getId())
//                .productDetailResponse(ProductDetailResponse.formProductDetail(cartItem.getProductDetail()))
                .productDetailResponse(cartItem.getProductDetail())
                .quantity(cartItem.getQuantity())
                .productImageResponse(cartItem.getProductDetail().getProduct().getProductImages())
                .productName(cartItem.getProductDetail().getProduct().getName())
                .build();

        return cartItemResponse;
    }
}
