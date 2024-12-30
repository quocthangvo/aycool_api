package com.example.shopapp_api.dtos.requests.cart;

import com.example.shopapp_api.dtos.requests.product.ProductDTO;
import com.example.shopapp_api.dtos.requests.product.ProductDetailDTO;
import com.example.shopapp_api.entities.products.ProductDetail;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class CartItemDTO {
    //    private int id;
    //    private ProductDetail productDetail;
    private int quantity;

}
