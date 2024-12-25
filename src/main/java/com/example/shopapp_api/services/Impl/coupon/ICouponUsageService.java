package com.example.shopapp_api.services.Impl.coupon;

import com.example.shopapp_api.entities.coupon.Coupon;

public interface ICouponUsageService {
    void saveCouponUsage(Coupon coupon, int userId);
}
