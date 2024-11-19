package com.example.shopapp_api.dtos.responses.order;

import com.example.shopapp_api.dtos.responses.product.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderListResponse {
    private List<OrderResponse> orderResponseList; // truyền list
    private int totalPages;
}
