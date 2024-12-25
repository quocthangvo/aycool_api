package com.example.shopapp_api.entities.coupon;

import com.example.shopapp_api.entities.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Table(name = "coupon_usages")
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class CouponUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "used_at", nullable = false)
    private Date usedAt;


}
