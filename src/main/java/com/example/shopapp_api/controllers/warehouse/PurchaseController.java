package com.example.shopapp_api.controllers.warehouse;

import com.example.shopapp_api.dtos.requests.warehouse.PurchaseDTO;
import com.example.shopapp_api.dtos.requests.warehouse.PurchaseItemDTO;
import com.example.shopapp_api.dtos.responses.apiResponse.ApiResponse;
import com.example.shopapp_api.dtos.responses.apiResponse.MessageResponse;
import com.example.shopapp_api.dtos.responses.warehouse.PurchaseListResponse;
import com.example.shopapp_api.dtos.responses.warehouse.WarehouseListResponse;
import com.example.shopapp_api.dtos.responses.warehouse.WarehouseResponse;
import com.example.shopapp_api.entities.warehouse.Purchase;
import com.example.shopapp_api.services.Impl.warehouse.IPurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/purchases")
@RequiredArgsConstructor
public class PurchaseController {
    private final IPurchaseService purchaseService;

    @PostMapping("")
    public ResponseEntity<?> createPurchaseOrder(@RequestBody List<PurchaseDTO> purchaseDTOList) {
        try {
            List<Purchase> purchases = purchaseService.createPurchases(purchaseDTOList);
            return ResponseEntity.ok(new ApiResponse<>("Đơn nhập hàng đã được tạo thành công", purchases));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getALlWarehouse(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        try {
            //tạo PagesRequest từ thông tin page và limit
            PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("dateTime").descending());

            Page<Purchase> purchases = purchaseService.getAllPurchase(pageRequest);
            //tông số trang
            int totalPages = purchases.getTotalPages();//lấy ra tổng số trang
            long totalRecords = purchases.getTotalElements();
            List<Purchase> purchaseList = purchases.getContent();//từ productPgae lấy ra ds các product getContent

            PurchaseListResponse purchaseListResponse = (PurchaseListResponse
                    .builder()
                    .purchaseList(purchaseList)
                    .totalPages(totalPages)
                    .totalRecords(totalRecords)
                    .build());
            return ResponseEntity.ok(new ApiResponse<>("Thành công", purchaseListResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPurchaseById(@PathVariable int id) {
        Purchase purchase = purchaseService.getPurchaseById(id);
        return ResponseEntity.ok(new ApiResponse<>("Thành công", purchase));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePurchseById(@PathVariable int id) {
        purchaseService.deletePurchase(id);
        return ResponseEntity.ok(new MessageResponse("Thành công"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePurchase(@PathVariable int id,
                                            @RequestBody PurchaseItemDTO purchaseItemDTO) {
        Purchase update = purchaseService.updatePurchase(id, purchaseItemDTO);
        return ResponseEntity.ok(new ApiResponse<>("Cập nhật thành công", update));
    }
}
