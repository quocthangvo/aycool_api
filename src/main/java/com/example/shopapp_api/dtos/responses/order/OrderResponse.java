package com.example.shopapp_api.dtos.responses.order;

import com.example.shopapp_api.dtos.responses.BaseResponse;
import com.example.shopapp_api.dtos.responses.product.ProductResponse;
import com.example.shopapp_api.entities.orders.Order;
import com.example.shopapp_api.entities.orders.OrderDetail;
import com.example.shopapp_api.entities.orders.OrderStatus;
import com.example.shopapp_api.entities.products.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OrderResponse extends BaseResponse {
    private int id;

    @JsonProperty("user_id")
    private int userId;

    private String name;

    @JsonProperty("order_code")
    private String orderCode;

    @JsonProperty("address_id")
    private int addressId;

    private String note;

    @JsonProperty("order_date")
    private String orderDate;

    private OrderStatus status;

    @JsonProperty("total_money")
    private String totalMoney;


//    @JsonProperty("shipping_method")
//    private String shippingMethod;

    @JsonProperty("shipping_date")
    private LocalDateTime shippingDate;


    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("active")
    private Boolean active;

    private String statusDisplayName;

    @JsonProperty("order_details")
    private List<OrderDetailResponse> orderDetails;

    public static OrderResponse formOrder(Order order) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        String formattedTotalMoney = formatter.format(order.getTotalMoney());
        // Định dạng ngày tháng năm giờ phút mà không có giây
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedOrderDate = order.getOrderDate().format(formatterDate);
        String formattedShippingDate = order.getShippingDate().format(formatterDate);


        // Chuyển đổi danh sách orderDetails thành OrderDetailResponse
        List<OrderDetailResponse> orderDetailResponses = order.getOrderDetails().stream()
                .map(OrderDetailResponse::formOrderDetail) // Giả sử bạn có phương thức formOrderDetail() trong OrderDetailResponse
                .collect(Collectors.toList());

        OrderResponse orderResponse = OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .name(order.getUser().getFullName())
                .orderCode(order.getOrderCode())
                .orderDate(formattedOrderDate)
                .totalMoney(formattedTotalMoney)
                .active(order.getActive())
                .status(order.getStatus())
                .statusDisplayName(order.getStatus().getStatusDisplayName())
                .orderDetails(orderDetailResponses)
                .build();

        return orderResponse;
    }
}
