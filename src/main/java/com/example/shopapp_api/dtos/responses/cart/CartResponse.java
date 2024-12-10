package com.example.shopapp_api.dtos.responses.cart;

import com.example.shopapp_api.entities.cart.Cart;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CartResponse {

    private int id;

    @JsonProperty("user_id")
    private int user;

    @JsonProperty("items")
    private List<CartItemResponse> items;

    public static CartResponse fromCart(Cart cart) {
        List<CartItemResponse> cartItemResponses = cart.getItems().stream()
                .map(CartItemResponse::formCartItem)  // Assuming CartItemResponse has a static method to convert CartItem to CartItemResponse
                .toList();

        return CartResponse.builder()
                .id(cart.getId())
                .user(cart.getUser().getId())  // Assuming getUser() returns a User object with an ID
                .items(cartItemResponses)
                .build();
    }
}
