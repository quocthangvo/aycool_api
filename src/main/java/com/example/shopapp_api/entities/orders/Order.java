package com.example.shopapp_api.entities.orders;

import com.example.shopapp_api.entities.BaseEntity;
import com.example.shopapp_api.entities.coupon.Coupon;
import com.example.shopapp_api.entities.orders.status.OrderStatus;
import com.example.shopapp_api.entities.orders.status.PaymentMethod;
import com.example.shopapp_api.entities.orders.status.PaymentStatus;
import com.example.shopapp_api.entities.users.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "don_hang")
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tự dong759 tăng
    @Column(name = "ma_don_hang")
    private int id;

    @ManyToOne
    @JoinColumn(name = "ma_nguoi_dung")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ma_dia_chi")
    private Address address;

    @Column(name = "ma_sku_don_hang")
    private String orderCode;

    @Column(name = "ghi_chu", length = 250)
    private String note;

    @Column(name = "ngay_dat_hang")
    private LocalDateTime OrderDate;

    @Column(name = "trang_thai")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "dang_giao_hang")
    private LocalDateTime shippingDate;

    @Column(name = "ngay_dang_xu_ly")
    private LocalDateTime processingDate;

    @Column(name = "ngay_giao_hang")
    private LocalDateTime deliveredDate;

    @Column(name = "ngay_huy")
    private LocalDateTime cancelledDate;

    @Column(name = "phuong_thuc_thanh_toan")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "tong_tien")
    private Float totalMoney;

    @Column(name = "hoat_dong")
    private Boolean active;

    @OneToMany(mappedBy = "order")
    @JsonManagedReference
    private List<OrderDetail> orderDetails;

    @Column(name = "trang_thai_thanh_toan")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @ManyToOne
    @JoinColumn(name = "ma_giam_gia")
    private Coupon coupon;

    @Column(name = "tong_tien_da_giam")
    private Float totalMoneyAfterDiscount;

}
