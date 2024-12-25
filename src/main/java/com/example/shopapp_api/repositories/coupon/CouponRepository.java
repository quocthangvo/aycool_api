package com.example.shopapp_api.repositories.coupon;

import com.example.shopapp_api.entities.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {
    Optional<Coupon> findByCode(String code);
}
