package com.example.shopapp_api.dtos.requests.order;

import com.example.shopapp_api.entities.orders.status.OrderStatus;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class OrderStatusDTO {
    private OrderStatus status;
}
