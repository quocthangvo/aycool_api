package com.example.shopapp_api.dtos.responses.product.products;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ProductListResponse {
    private List<ProductResponse> productResponseList; // truyền list
    private int totalPages;
    private long totalRecords;
}
