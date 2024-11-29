package com.example.shopapp_api.dtos.responses.price;

import com.example.shopapp_api.dtos.responses.order.OrderResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceListResponse {
    private List<PriceResponse> priceResponseList; // truyền list
    private int totalPages;
    private long totalRecords;
}
