package com.example.shopapp_api.services.Serv.cart;

import com.example.shopapp_api.dtos.requests.cart.CartItemDTO;
import com.example.shopapp_api.dtos.responses.cart.CartItemResponse;
import com.example.shopapp_api.dtos.responses.cart.CartResponse;
import com.example.shopapp_api.dtos.responses.product.ProductDetailResponse;
import com.example.shopapp_api.entities.cart.Cart;
import com.example.shopapp_api.entities.cart.CartItem;
import com.example.shopapp_api.entities.products.Product;
import com.example.shopapp_api.entities.products.ProductDetail;
import com.example.shopapp_api.entities.users.User;
import com.example.shopapp_api.repositories.cart.CartItemRepository;
import com.example.shopapp_api.repositories.cart.CartRepository;
import com.example.shopapp_api.repositories.product.ProductDetailRepository;
import com.example.shopapp_api.repositories.product.ProductRepository;
import com.example.shopapp_api.repositories.user.UserRepository;
import com.example.shopapp_api.services.Impl.cart.ICartServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService implements ICartServices {
    private final CartRepository cartRepository;
    private final ProductDetailRepository productDetailRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    @Override
    public CartResponse getCartByUserId(int userId) {
        // Lấy giỏ hàng của người dùng từ cơ sở dữ liệu
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Chuyển đổi giỏ hàng thành CartResponse
        List<CartItemResponse> cartItemResponses = cart.getItems().stream()
                .map(item -> CartItemResponse.formCartItem(item))
                .collect(Collectors.toList());

        return CartResponse.builder()
                .user(cart.getUser().getId())  // user_id
                .items(cartItemResponses)      // các mục trong giỏ hàng
                .build();
    }

    @Override
    public CartResponse addToCart(int userId, int productDetailId, int quantity) {
        // Lấy giỏ hàng của người dùng (nếu chưa có thì tạo mới)
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = new User();
                    user.setId(userId);
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        // Lấy sản phẩm từ cơ sở dữ liệu
        ProductDetail productDetail = productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new RuntimeException("ProductDetail not found"));


        // Kiểm tra xem sản phẩm này đã có trong giỏ chưa
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductDetail().getId() == productDetailId)
                .findFirst();

        if (existingItem.isPresent()) {
            // Nếu sản phẩm đã có trong giỏ hàng, cập nhật số lượng
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            // Nếu sản phẩm chưa có, thêm sản phẩm mới vào giỏ hàng
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProductDetail(productDetail);
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
        }

        // Lưu lại giỏ hàng
//        return cartRepository.save(cart);
        // Lưu lại giỏ hàng
        cart = cartRepository.save(cart);

        // Chuyển giỏ hàng thành response
        List<CartItemResponse> cartItemResponses = cart.getItems().stream()
                .map(item -> CartItemResponse.formCartItem(item))
                .collect(Collectors.toList());

//        return new CartResponse(cart.getUser().getId(), cartItemResponses);

        return CartResponse.builder()
                .user(cart.getUser().getId()) // Set user_id
                .items(cartItemResponses)     // Set items
                .build();
    }


    // Xóa một sản phẩm trong giỏ hàng
    @Override
    public void deleteCartItem(int itemId) {
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found with id: " + itemId));

        cartItemRepository.delete(cartItem);
    }

    @Override
    public CartResponse updateQuantity(int cartItemId, CartItemDTO cartItemDTO) throws Exception {

        // Find the cart item by ID
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new Exception("Sản phẩm không tồn tại trong giỏ"));

        // Validate quantity (optional, ensure it's a positive number and not exceeding the limit)
        if (cartItemDTO.getQuantity() <= 0 || cartItemDTO.getQuantity() > 99) {
            throw new Exception("Số lượng phải lớn hơn 0 và không quá 99");
        }

        // Update the quantity of the cart item using the quantity from DTO
        cartItem.setQuantity(cartItemDTO.getQuantity());
        cartItemRepository.save(cartItem);  // Save the updated cart item

        // Optionally, fetch and return the updated cart if needed
        Cart cart = cartRepository.findById(cartItem.getCart().getId())
                .orElseThrow(() -> new Exception("Giỏ hàng không tồn tại"));

        return CartResponse.fromCart(cart);
    }

}
