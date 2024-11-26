package com.example.shopapp_api.controllers.orders;

import com.example.shopapp_api.dtos.requests.order.OrderDTO;
import com.example.shopapp_api.dtos.requests.order.OrderStatusDTO;
import com.example.shopapp_api.dtos.responses.apiResponse.ApiResponse;
import com.example.shopapp_api.dtos.responses.apiResponse.MessageResponse;
import com.example.shopapp_api.dtos.responses.order.OrderListResponse;
import com.example.shopapp_api.dtos.responses.order.OrderResponse;
import com.example.shopapp_api.dtos.responses.order.StatusResponse;
import com.example.shopapp_api.dtos.responses.product.ProductListResponse;
import com.example.shopapp_api.dtos.responses.product.ProductResponse;
import com.example.shopapp_api.services.Impl.order.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor

public class OrderController {
    private final IOrderService orderService;

    @PostMapping("")
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderDTO orderDTO,
            BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            OrderResponse createOrder = orderService.createOrder(orderDTO);
            return ResponseEntity.ok(new ApiResponse<>("Đặt hàng thành công thành công", createOrder));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));

        }
    }


    @GetMapping("")
    public ResponseEntity<?> getALlOrders(
            //truyền productListResponse thì k cần List<>
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        try {
            //tạo PagesRequest từ thông tin page và limit
            PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());

            Page<OrderResponse> orderPage = orderService.getAllOrders(pageRequest);
            //tông số trang
            int totalPages = orderPage.getTotalPages();//lấy ra tổng số trang
            List<OrderResponse> orderList = orderPage.getContent();//từ productPage lấy ra ds các product getContent

            OrderListResponse orderListResponse = (OrderListResponse
                    .builder()
                    .orderResponseList(orderList)
                    .totalPages(totalPages)
                    .build());
            return ResponseEntity.ok(new ApiResponse<>("Thành công", orderListResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }

    }


    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getOrderByUserId(
            @Valid @PathVariable("user_id") int userId
    ) {
        try {
            List<OrderResponse> orders = orderService.findByUserId(userId);
            return ResponseEntity.ok(new ApiResponse<>("Thành công", orders));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));

        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(
            @Valid @PathVariable("id") int orderId
    ) {
        try {
            OrderResponse existingOrder = orderService.getOrderById(orderId);
            return ResponseEntity.ok(new ApiResponse<>(" thành công", existingOrder));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));

        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteOrder(@PathVariable int id) {
        //xóa mềm, không xóa trong database
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.ok(new MessageResponse(String.format("Xóa đơn hàng có id = %d thành công", id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    //admin làm
    public ResponseEntity<?> updateOrder(
            @PathVariable("id") int id,
            @Valid @RequestBody OrderStatusDTO orderStatusDTO) {

        try {
            StatusResponse updateOrder = orderService.updateOrder(id, orderStatusDTO);
            return ResponseEntity.ok(new ApiResponse<>("Cập nhật đơn hàng thành công", updateOrder));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));

        }
    }
}
