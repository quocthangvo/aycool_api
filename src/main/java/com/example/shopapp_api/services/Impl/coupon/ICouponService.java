package com.example.shopapp_api.services.Impl.coupon;

import com.example.shopapp_api.dtos.requests.category.CategoryDTO;
import com.example.shopapp_api.dtos.requests.coupon.CouponDTO;
import com.example.shopapp_api.entities.categories.Category;
import com.example.shopapp_api.entities.coupon.Coupon;
import com.example.shopapp_api.exceptions.DataNotFoundException;

import java.util.List;

public interface ICouponService {
    Coupon createCoupon(CouponDTO couponDTO) throws DataNotFoundException;

    Coupon getCouponById(int id);

    List<Coupon> getAllCoupons();

    void deleteCoupon(int id);

    Coupon updateCoupon(int couponId, CouponDTO couponDTO) throws DataNotFoundException;

    List<Coupon> getAllCouponsStatus();

//    void applyCoupon(String code, int userId);
}
