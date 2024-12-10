package com.example.shopapp_api.dtos.responses.order;

import com.example.shopapp_api.dtos.responses.BaseResponse;
import com.example.shopapp_api.entities.orders.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.text.DecimalFormat;
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
//
//    @JsonProperty("address_id")
//    private int addressId;

    private String note;

    @JsonProperty("order_date")
    private String orderDate;

    private OrderStatus status;

    @JsonProperty("total_money")
    private String totalMoney;


    @JsonProperty("processing_date")
    private String processingDate;

    @JsonProperty("shipping_date")
    private String shippingDate;

    @JsonProperty("delivered_date")
    private String deliveredDate;

    @JsonProperty("cancelled_date")
    private String cancelledDate;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("active")
    private Boolean active;

    private String statusDisplayName;

    @JsonProperty("order_details")
    private List<OrderDetailResponse> orderDetails;

    @JsonProperty("payment_status")
    private PaymentStatus paymentStatus;

    @JsonProperty("status_display_payment")
    private String statusDisplayPayment;

    @JsonProperty("address")
    private Address address;


    public static OrderResponse formOrder(Order order) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        String formattedTotalMoney = formatter.format(order.getTotalMoney());

        // Định dạng ngày tháng năm giờ phút theo mẫu "dd-MM-yyyy HH:mm"
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedOrderDate = order.getOrderDate().format(formatterDate);

        // Chuyển đổi các ngày trạng thái sang định dạng mong muốn
        String formattedProcessingDate = (order.getProcessingDate() != null)
                ? order.getProcessingDate().format(formatterDate)
                : null;

        String formattedShippingDate = (order.getShippingDate() != null)
                ? order.getShippingDate().format(formatterDate)
                : null;

        String formattedDeliveredDate = (order.getDeliveredDate() != null)
                ? order.getDeliveredDate().format(formatterDate)
                : null;

        String formattedCancelledDate = (order.getCancelledDate() != null)
                ? order.getCancelledDate().format(formatterDate)
                : null;

        // Chuyển đổi danh sách orderDetails thành OrderDetailResponse
        List<OrderDetailResponse> orderDetailResponses = order.getOrderDetails().stream()
                .map(OrderDetailResponse::formOrderDetail)
                .collect(Collectors.toList());


        if (order.getPaymentStatus() == null) {
            // Nếu paymentStatus là null, gán giá trị mặc định
            order.setPaymentStatus(PaymentStatus.NOPAYMENT); // Hoặc giá trị mặc định khác
        }

        // Tạo đối tượng OrderResponse
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
                .processingDate(formattedProcessingDate)
                .shippingDate(formattedShippingDate)
                .deliveredDate(formattedDeliveredDate)
                .cancelledDate(formattedCancelledDate)
                .orderDetails(orderDetailResponses)
                .paymentStatus(order.getPaymentStatus())
                .statusDisplayPayment(order.getPaymentStatus().getStatusDisplayPayment())
                .address(order.getAddress())
                .build();

        return orderResponse;
    }


}
