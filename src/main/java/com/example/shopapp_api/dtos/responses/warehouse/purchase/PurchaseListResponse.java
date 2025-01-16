package com.example.shopapp_api.dtos.responses.warehouse.purchase;

import com.example.shopapp_api.entities.warehouse.Purchase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseListResponse {
    private List<PurchaseResponse> purchaseList; // truyền list
    private int totalPages;
    private long totalRecords;
}
