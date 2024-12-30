package com.example.shopapp_api.dtos.requests.order;

import com.example.shopapp_api.entities.orders.status.OrderStatus;
import com.example.shopapp_api.entities.orders.status.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor

public class OrderDTO {

    @Min(value = 1, message = "user id must be > 1")
    @JsonProperty("user_id")
    private int userId;

    @Min(value = 1, message = "address id must be > 1")
    @JsonProperty("address_id")
    private int addressId;

    @JsonProperty("order_code")
    private String orderCode;

    private String note;

    @JsonProperty("total_money")
    @Min(value = 0, message = "Total money must be > 0")
    private float totalMoney;


    @JsonProperty("order_date")
    private LocalDateTime OrderDate;


    @JsonProperty("shipping_date")
    private LocalDateTime shippingDate;

    @JsonProperty("payment_method")
    private PaymentMethod paymentMethod;


    private OrderStatus status;

    private boolean active;

    @JsonProperty("order_details")
    private List<OrderDetailDTO> orderDetails;

    @JsonProperty("selected_items")
    private List<Integer> selectedItems;

    @JsonProperty("transaction_no")
    private String transactionNo;

    @JsonProperty("amount")
    private String amount;

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("coupon_id")
    private Integer couponId;

    @JsonProperty("total_money_after_discount")
    private Float totalMoneyAfterDiscount;
}
