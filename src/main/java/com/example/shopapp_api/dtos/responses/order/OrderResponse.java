package com.example.shopapp_api.dtos.responses.order;

import com.example.shopapp_api.dtos.responses.BaseResponse;
import com.example.shopapp_api.entities.orders.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OrderResponse extends BaseResponse {
    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("address_id")
    private int addressId;

    private String note;

    @JsonProperty("order_date")
    private LocalDateTime orderDate;

    private OrderStatus status;

    @JsonProperty("total_money")
    private float totalMoney;


//    @JsonProperty("shipping_method")
//    private String shippingMethod;

    @JsonProperty("shipping_date")
    private LocalDateTime shippingDate;


    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("active")
    private Boolean active;
}
