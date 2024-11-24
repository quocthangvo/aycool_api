package com.example.shopapp_api.dtos.requests.cart;

import com.example.shopapp_api.dtos.requests.auth.UserDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class CartDTO {
    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("product_detail_id")
    private int productDetailId;

    private int quantity;

}
