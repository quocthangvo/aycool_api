package com.example.shopapp_api.repositories.coupon;

import com.example.shopapp_api.entities.coupon.CouponUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponUsageRepository extends JpaRepository<CouponUsage, Integer> {
    boolean existsByCouponIdAndUserId(int couponId, int userId);
}
