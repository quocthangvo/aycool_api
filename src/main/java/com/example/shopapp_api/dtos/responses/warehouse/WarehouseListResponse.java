package com.example.shopapp_api.dtos.responses.warehouse;

import com.example.shopapp_api.entities.warehouse.Warehouse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class WarehouseListResponse {
    private List<WarehouseResponse> warehouseResponseList; // truyền list
    private int totalPages;
    private long totalRecords;
}
