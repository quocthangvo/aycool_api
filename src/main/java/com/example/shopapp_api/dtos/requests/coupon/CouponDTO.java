package com.example.shopapp_api.dtos.requests.coupon;

import com.example.shopapp_api.entities.coupon.DiscountType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class CouponDTO {

    private String code;

    @JsonProperty("discount_value")
    private Float discountValue;

    @JsonProperty("discount_type")
    private DiscountType discountType;

    private String description;

    // giá trị đơn hàng được dùng
    @JsonProperty("min_order_value")
    private Float minOrderValue;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("end_date")
    private LocalDate endDate;

    @JsonProperty("usage_limit")
    private Integer usageLimit;

    private boolean status;
}
