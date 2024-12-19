package com.example.shopapp_api.dtos.responses.order;

import com.example.shopapp_api.entities.orders.Order;
import com.example.shopapp_api.entities.orders.status.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class StatusResponse {

    @JsonProperty("user_id")
    private int userId;

    private OrderStatus status;

    public static StatusResponse formStatus(Order order) {


        // Tạo đối tượng OrderResponse
        StatusResponse statusResponse = StatusResponse.builder()
                .userId(order.getUser().getId())
                .status(order.getStatus())
                .build();

        return statusResponse;
    }
}
