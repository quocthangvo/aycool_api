package com.example.shopapp_api.controllers.warehouse;

import com.example.shopapp_api.dtos.responses.apiResponse.ApiResponse;
import com.example.shopapp_api.dtos.responses.product.products.ProductResponse;
import com.example.shopapp_api.dtos.responses.warehouse.group.WarehouseGroupListResponse;
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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/warehouse")
@RequiredArgsConstructor
public class WarehouseController {
    private final IWarehouseService warehouseService;

    @GetMapping("/all")
    public ResponseEntity<?> getALl(
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


    // admin dùng
    @GetMapping("")
    public ResponseEntity<?> getALlWarehouse(
            @RequestParam(value = "skuName", required = false, defaultValue = "") String skuName,
            @RequestParam(value = "subCategoryId", required = false, defaultValue = "") Integer subCategoryId,
            @RequestParam(value = "materialId", required = false, defaultValue = "") Integer materialId,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        try {
            //tạo PagesRequest từ thông tin page và limit
            PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());

            Page<WarehouseResponse> warehouses = warehouseService
                    .searchWarehousesBySkuNameAndSubCategory(skuName, subCategoryId, materialId, pageRequest);
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

    @GetMapping("/grouped/sub-category")
    public ResponseEntity<?> getGroupedWarehouse(
            @RequestParam(value = "subCategoryId", required = false, defaultValue = "") int subCategoryId,
            @RequestParam(value = "colorIds", required = false, defaultValue = "") List<Integer> colorIds,
            @RequestParam(value = "sizeIds", required = false, defaultValue = "") List<Integer> sizeIds,
            @RequestParam(value = "materialId", required = false, defaultValue = "") Integer materialId,
            @RequestParam(value = "page", required = false) int page,
            @RequestParam(value = "limit", required = false) int limit
    ) {
        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<WarehouseGroupResponse> response = warehouseService.getGroupedWarehouseByFilter(subCategoryId,
                colorIds == null ? Collections.emptyList() : colorIds,
                sizeIds == null ? Collections.emptyList() : sizeIds,
                materialId,
                pageRequest);

        if (response == null || response.getContent().isEmpty()) {
            // Trả về danh sách rỗng khi không có sản phẩm nào phù hợp
            return ResponseEntity.ok(new ApiResponse<>("Không có sản phẩm phù hợp", Collections.emptyList()));
        }

        // tông số trang
        int totalPages = response.getTotalPages();
        long totalRecords = response.getTotalElements();
        List<WarehouseGroupResponse> warehouseList = response.getContent();

        WarehouseGroupListResponse warehouseListResponse = WarehouseGroupListResponse.builder()
//                .warehouseGroupResponseList(warehouseList)
                .warehouseGroupResponseList(warehouseList.stream()
                        .sorted(Comparator.comparing(WarehouseGroupResponse::getCreatedAt).reversed())
                        .collect(Collectors.toList()))  // Sắp xếp theo createdAt mới nhất
                .totalPages(totalPages)
                .totalRecords(totalRecords)
                .build();
        return ResponseEntity.ok(new ApiResponse<>("Thành công", warehouseListResponse));
    }


    //user all sp
    @GetMapping("/grouped")
    public ResponseEntity<?> getGroupedWarehouse(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "limit") int limit,
            @RequestParam(value = "searchTerm", required = false, defaultValue = "") String searchTerm,
            @RequestParam(value = "subCategoryId", required = false, defaultValue = "") Integer subCategoryId
    ) {
        try {
            //tạo PagesRequest từ thông tin page và limit
//            PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());
            PageRequest pageRequest = PageRequest.of(page, limit);


            Page<WarehouseGroupResponse> warehouses = warehouseService
                    .getGroupedWarehouse(pageRequest, searchTerm, subCategoryId);
            //tông số trang
            int totalPages = warehouses.getTotalPages();//lấy ra tổng số trang
            long totalRecords = warehouses.getTotalElements();
            List<WarehouseGroupResponse> warehouseList = warehouses.getContent();//từ productPgae lấy ra ds các product getContent

            WarehouseGroupListResponse warehouseListResponse = (WarehouseGroupListResponse
                    .builder()
                    .warehouseGroupResponseList(warehouseList)
//                    .warehouseGroupResponseList(warehouseList.stream()
//                            .sorted(Comparator.comparing(WarehouseGroupResponse::getCreatedAt).reversed())
//                            .collect(Collectors.toList()))  // Sắp xếp theo createdAt mới nhất
                    .totalPages(totalPages)
                    .totalRecords(totalRecords)
                    .build());
            return ResponseEntity.ok(new ApiResponse<>("Thành công", warehouseListResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }

    @GetMapping("/categoryId")
    public ResponseEntity<?> getWarehouseByCategory(
            @RequestParam int categoryId,
            @RequestParam(value = "page", required = false, defaultValue = "") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "") int limit) {
        PageRequest pageRequest = PageRequest.of(page, limit);

        Page<WarehouseGroupResponse> response = warehouseService.getGroupedProductsByCategoryId(pageRequest, categoryId);
        // tông số trang
        int totalPages = response.getTotalPages();
        long totalRecords = response.getTotalElements();
        List<WarehouseGroupResponse> warehouseList = response.getContent();

        WarehouseGroupListResponse warehouseListResponse = (WarehouseGroupListResponse
                .builder()
//                    .warehouseGroupResponseList(warehouseList)
                .warehouseGroupResponseList(warehouseList.stream()
                        .sorted(Comparator.comparing(WarehouseGroupResponse::getCreatedAt).reversed())
                        .collect(Collectors.toList()))  // Sắp xếp theo createdAt mới nhất
                .totalPages(totalPages)
                .totalRecords(totalRecords)
                .build());
        return ResponseEntity.ok(new ApiResponse<>("Thành công", warehouseListResponse));
    }


}
