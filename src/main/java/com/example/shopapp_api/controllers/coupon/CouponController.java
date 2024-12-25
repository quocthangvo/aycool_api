package com.example.shopapp_api.controllers.coupon;

import com.example.shopapp_api.dtos.requests.coupon.CouponDTO;
import com.example.shopapp_api.dtos.responses.apiResponse.ApiResponse;
import com.example.shopapp_api.dtos.responses.apiResponse.MessageResponse;
import com.example.shopapp_api.entities.coupon.Coupon;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.services.Impl.coupon.ICouponService;
import com.example.shopapp_api.services.Serv.coupon.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final ICouponService couponService;

    @PostMapping("")
    public ResponseEntity<?> createCoupon(@RequestBody CouponDTO couponDTO,
                                          BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            Coupon createdCoupon = couponService.createCoupon(couponDTO);
            return ResponseEntity.ok(new ApiResponse<>("Thêm thành công", createdCoupon));
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllCoupons() {
        try {
            List<Coupon> coupons = couponService.getAllCoupons();
            return ResponseEntity.ok(new ApiResponse<>("Lấy tất cả mã giảm giá thành công", coupons));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCouponById(@PathVariable int id) {
        try {
            Coupon coupon = couponService.getCouponById(id);
            return ResponseEntity.ok(new ApiResponse<>("Lấy mã giảm giá thành công", coupon));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCoupon(@PathVariable int id) {
        try {
            couponService.deleteCoupon(id);
            return ResponseEntity.ok(new MessageResponse("Xóa mã giảm giá thành công"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Lỗi: " + e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCoupon(@PathVariable int id, @RequestBody CouponDTO couponDTO) {
        try {
            Coupon updatedCoupon = couponService.updateCoupon(id, couponDTO);
            return ResponseEntity.ok(new ApiResponse<>("Cập nhật mã giảm giá thành công", updatedCoupon));
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }

    @GetMapping("/status")
    public ResponseEntity<?> getAllCouponsStatus() {
        try {
            List<Coupon> coupons = couponService.getAllCouponsStatus();
            return ResponseEntity.ok(new ApiResponse<>("Lấy tất cả mã giảm giá thành công", coupons));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }

//    @PostMapping("/apply")
//    public ResponseEntity<?> applyCoupon(@RequestParam String code, @RequestParam int userId) {
//        try {
//            // Áp dụng mã giảm giá
//            couponService.applyCoupon(code, userId);
//            return ResponseEntity.ok(new MessageResponse("Mã giảm giá đã được áp dụng thành công"));
//        } catch (IllegalArgumentException e) {
//            // Trả về lỗi nếu có bất kỳ vấn đề nào xảy ra
//            return ResponseEntity.badRequest().body(new MessageResponse("Lỗi: " + e.getMessage()));
//        }
//    }
}
