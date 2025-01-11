package com.example.shopapp_api.dtos.responses.warehouse.group;

import com.example.shopapp_api.dtos.responses.warehouse.WarehouseResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarehouseGroupListResponse {
    private List<WarehouseGroupResponse> warehouseGroupResponseList; // truyền list
    private int totalPages;
    private long totalRecords;
}
