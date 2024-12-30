package com.example.shopapp_api.entities.coupon;

import com.example.shopapp_api.entities.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Table(name = "phieu_su_dung")
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class CouponUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_phieu_su_dung")
    private int id;

    @ManyToOne
    @JoinColumn(name = "ma_giam_gia")
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(name = "ma_nguoi_dung")
    private User user;

    @Column(name = "so_lan_su_dung")
    private Integer usageCount;


}
