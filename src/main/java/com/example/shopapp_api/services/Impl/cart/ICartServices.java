package com.example.shopapp_api.services.Impl.cart;

import com.example.shopapp_api.dtos.requests.cart.CartItemDTO;
import com.example.shopapp_api.dtos.responses.cart.CartResponse;
import com.example.shopapp_api.entities.cart.Cart;
import com.example.shopapp_api.entities.cart.CartItem;

import java.util.List;

public interface ICartServices {
    CartResponse getCartByUserId(int userId);

    CartResponse addToCart(int userId, int productDetailId, int quantity);

    void deleteCartItem(int itemId);

    CartResponse updateQuantity(int cartItemId, CartItemDTO cartItemDTO) throws Exception;
}
