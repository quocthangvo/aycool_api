package com.example.shopapp_api.entities.coupon;

import com.example.shopapp_api.entities.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Table(name = "phieu_giam_gia")
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class Coupon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tự dong759 tăng
    @Column(name = "ma_giam_gia")
    private int id;

    @Column(name = "code")
    private String code;

    @Column(name = "kieu_giam_gia")
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Column(name = "gia_tri_giam_gia")
    private Float discountValue;

    @Column(name = "mo_ta")
    private String description;

    // giá trị đơn hàng được dùng
    @Column(name = "gia_tri_don_hang_toi_thieu")
    private Float minOrderValue;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "ngay_bat_dau")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "ngay_ket_thuc")
    private LocalDate endDate;

    @Column(name = "gioi_han_su_dung")
    private Integer usageLimit;


    @Column(name = "trang_thai")
    private boolean status;

    @Column(name = "mo_ta_giam_gia")
    private String discountDescription;

    // Phương thức này sẽ trả về mô tả loại giảm giá
    public String getDiscountDescription() {
        return discountType != null ? discountType.getDiscountType() : "Không xác định";
    }

    @Column(name = "so_lan_su_dung")
    private Integer usageCount; // Số lượng đã sử dụng

    @Column(name = "gioi_han_su_dung_moi_nguoi")
    private Integer maxUsagePerUser; // Số lần tối đa mỗi người dùng có thể sử dụng

}
