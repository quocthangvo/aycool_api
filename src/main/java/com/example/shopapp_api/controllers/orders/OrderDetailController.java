package com.example.shopapp_api.controllers.orders;

import com.example.shopapp_api.dtos.requests.order.OrderDetailDTO;
import com.example.shopapp_api.dtos.requests.order.UpdateOrderDetailDTO;
import com.example.shopapp_api.dtos.responses.apiResponse.ApiResponse;
import com.example.shopapp_api.dtos.responses.apiResponse.MessageResponse;
import com.example.shopapp_api.dtos.responses.order.OrderDetailResponse;
import com.example.shopapp_api.dtos.responses.order.TotalResponse;
import com.example.shopapp_api.entities.orders.OrderDetail;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.services.Serv.order.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO orderDetailDTO,
            BindingResult result) {


        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            OrderDetail createOrderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
            OrderDetailResponse orderDetailResponse = OrderDetailResponse.formOrderDetail(createOrderDetail);
            return ResponseEntity.ok().body(new ApiResponse<>("Thành công", orderDetailResponse)); // trả về response
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrdeDetail(
            @Valid @PathVariable("id") int id
    ) {
        try {
            OrderDetailResponse orderDetail = orderDetailService.getOrderDetailById(id);
            return ResponseEntity.ok(new ApiResponse<>("Thành công", orderDetail));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetailByOrderId(
            @Valid @PathVariable("orderId") int orderId
    ) {
        try {
            List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
            List<OrderDetailResponse> orderDetailResponses = orderDetails
                    .stream()
                    .map(OrderDetailResponse::formOrderDetail)
                    .toList();
            return ResponseEntity.ok(new ApiResponse<>("Thành công", orderDetailResponses));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));

        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteOrder(@PathVariable("id") int id) {
        try {
            orderDetailService.deleteOrderDetail(id);
            return ResponseEntity.ok(new MessageResponse(String.format("Xóa chi tiết đơn hàng có id = %d thành công", id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @PathVariable("id") int id,
            @Valid @RequestBody UpdateOrderDetailDTO updateOrderDetailDTO) throws DataNotFoundException {
        try {
            OrderDetailResponse orderDetail = orderDetailService.updateOrderDetail(id, updateOrderDetailDTO);
            return ResponseEntity.ok(new ApiResponse<>("Cập nhật thành công", orderDetail));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));

        }
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable int orderId) {
        try {
            TotalResponse response = orderDetailService.getTotal(orderId);
            return ResponseEntity.ok(new ApiResponse<>("Thành công", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }

    @GetMapping("/top-selling")
    public ResponseEntity<?> getTopSellingProduct() {
        OrderDetailResponse order = orderDetailService.getTopSellingProduct();
        return ResponseEntity.ok(new ApiResponse<>("Thành công", order));

    }

    @GetMapping("/top3-selling")
    public ResponseEntity<ApiResponse<List<OrderDetailResponse>>> getTopSellingProducts() {
        List<OrderDetailResponse> order = orderDetailService.getTopSellingProducts();
        return ResponseEntity.ok(new ApiResponse<>("Thành công", order));
    }

    @GetMapping("/low-selling")
    public ResponseEntity<?> getLowSellingProduct() {
        OrderDetailResponse order = orderDetailService.getLowSellingProduct();
        return ResponseEntity.ok(new ApiResponse<>("Thành công", order));

    }

    @GetMapping("/low3-selling")
    public ResponseEntity<ApiResponse<List<OrderDetailResponse>>> getLowSellingProducts() {
        List<OrderDetailResponse> order = orderDetailService.getLowSellingProducts();
        return ResponseEntity.ok(new ApiResponse<>("Thành công", order));
    }
}
