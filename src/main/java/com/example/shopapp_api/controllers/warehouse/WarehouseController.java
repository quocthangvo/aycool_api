package com.example.shopapp_api.controllers.warehouse;

import com.example.shopapp_api.dtos.responses.apiResponse.ApiResponse;
import com.example.shopapp_api.dtos.responses.warehouse.group.WarehouseGroupResponse;
import com.example.shopapp_api.dtos.responses.warehouse.WarehouseListResponse;
import com.example.shopapp_api.dtos.responses.warehouse.WarehouseResponse;
import com.example.shopapp_api.services.Impl.warehouse.IWarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/warehouse")
@RequiredArgsConstructor
public class WarehouseController {
    private final IWarehouseService warehouseService;


    @GetMapping("")
    public ResponseEntity<?> getALlWarehouse(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        try {
            //tạo PagesRequest từ thông tin page và limit
            PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());

            Page<WarehouseResponse> warehouses = warehouseService.getAllWarehouse(pageRequest);
            //tông số trang
            int totalPages = warehouses.getTotalPages();//lấy ra tổng số trang
            long totalRecords = warehouses.getTotalElements();
            List<WarehouseResponse> warehouseList = warehouses.getContent();//từ productPgae lấy ra ds các product getContent

            WarehouseListResponse warehouseListResponse = (WarehouseListResponse
                    .builder()
                    .warehouseResponseList(warehouseList)
                    .totalPages(totalPages)
                    .totalRecords(totalRecords)
                    .build());
            return ResponseEntity.ok(new ApiResponse<>("Thành công", warehouseListResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }

    }

    @GetMapping("/grouped")
    public ResponseEntity<ApiResponse<List<WarehouseGroupResponse>>> getGroupedWarehouse() {
        List<WarehouseGroupResponse> response = warehouseService.getGroupedWarehouse();
        return ResponseEntity.ok(new ApiResponse<>("Thành công", response));
    }
}
