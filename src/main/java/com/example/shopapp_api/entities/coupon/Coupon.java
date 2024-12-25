package com.example.shopapp_api.entities.coupon;

import com.example.shopapp_api.entities.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Table(name = "coupons")
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class Coupon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tự dong759 tăng
    private int id;

    @Column(name = "code")
    private String code;

    @Column(name = "discount_type")
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Column(name = "discount_value")
    private Float discountValue;

    @Column(name = "description")
    private String description;

    // giá trị đơn hàng được dùng
    @Column(name = "min_order_value")
    private Float minOrderValue;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "start_date")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "usage_limit")
    private Integer usageLimit;


    @Column(name = "status")
    private boolean status;

    private String discountDescription;

    // Phương thức này sẽ trả về mô tả loại giảm giá
    public String getDiscountDescription() {
        return discountType != null ? discountType.getDiscountType() : "Không xác định";
    }
}
