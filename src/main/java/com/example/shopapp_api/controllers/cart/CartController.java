package com.example.shopapp_api.controllers.cart;

import com.example.shopapp_api.dtos.requests.cart.CartDTO;
import com.example.shopapp_api.dtos.requests.cart.CartItemDTO;
import com.example.shopapp_api.dtos.responses.apiResponse.ApiResponse;
import com.example.shopapp_api.dtos.responses.apiResponse.MessageResponse;
import com.example.shopapp_api.dtos.responses.cart.CartResponse;
import com.example.shopapp_api.entities.cart.Cart;
import com.example.shopapp_api.entities.cart.CartItem;
import com.example.shopapp_api.services.Impl.cart.ICartServices;
import com.example.shopapp_api.services.Serv.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/carts")
@RequiredArgsConstructor
public class CartController {
    private final ICartServices cartService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<CartResponse>> getCartByUserId(@PathVariable int userId) {
        CartResponse carts = cartService.getCartByUserId(userId);

        return ResponseEntity.ok(new ApiResponse<>("Thành công", carts));
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartResponse>> addToCart(
            @RequestBody CartDTO cartDTO) {
        CartResponse cart = cartService.addToCart(cartDTO.getUserId(), cartDTO.getProductDetailId(), cartDTO.getQuantity());
        return ResponseEntity.ok(new ApiResponse<>("Thành công", cart));
    }


    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable int itemId) {
        cartService.deleteCartItem(itemId);
        return ResponseEntity.ok(new MessageResponse("Thành công"));
    }

    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<?> updateCartItemQuantity(@PathVariable int cartItemId,
                                                    @RequestBody CartItemDTO cartItemDTO) {
        try {
            CartResponse updatedCart = cartService.updateQuantity(cartItemId, cartItemDTO);
            return ResponseEntity.ok(new ApiResponse<>("Cập nhật giỏ hàng thành công", updatedCart));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("Cập nhật thất bại", null));
        }
    }
}
