package com.example.shopapp_api.repositories.coupon;

import com.example.shopapp_api.entities.coupon.Coupon;
import com.example.shopapp_api.entities.coupon.CouponUsage;
import com.example.shopapp_api.entities.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponUsageRepository extends JpaRepository<CouponUsage, Integer> {
    Optional<CouponUsage> findByUserAndCoupon(User user, Coupon coupon);
}
