package com.example.shopapp_api.services.Serv.coupon;

import com.example.shopapp_api.entities.coupon.Coupon;
import com.example.shopapp_api.entities.coupon.CouponUsage;
import com.example.shopapp_api.repositories.coupon.CouponRepository;
import com.example.shopapp_api.repositories.coupon.CouponUsageRepository;
import com.example.shopapp_api.repositories.user.UserRepository;
import com.example.shopapp_api.services.Impl.coupon.ICouponUsageService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CouponUsageService implements ICouponUsageService {
    private final CouponUsageRepository couponUsageRepository;
    public final CouponRepository couponRepository;
    public final UserRepository userRepository;

//    @Override
//    public void applyCoupon(String code, int userId) {
//        // Tìm mã giảm giá
//        Coupon coupon = couponRepository.findByCode(code).orElseThrow(() ->
//                new IllegalArgumentException("Mã giảm giá không tồn tại.")
//        );
//
//        // Kiểm tra thời hạn mã giảm giá
//        Date now = new Date();
//        if (now.before(coupon.getStartDate()) || now.after(coupon.getEndDate())) {
//            throw new IllegalArgumentException("Mã giảm giá đã hết hạn.");
//        }
//
//        // Kiểm tra nếu người dùng đã sử dụng mã này
//        boolean alreadyUsed = couponUsageRepository.existsByCouponIdAndUserId(coupon.getId(), userId);
//        if (alreadyUsed) {
//            throw new IllegalArgumentException("Bạn đã sử dụng mã giảm giá này rồi.");
//        }
//
//        // Kiểm tra nếu mã giảm giá còn đủ số lần sử dụng
//        if (coupon.getUsageLimit() <= 0) {
//            throw new IllegalArgumentException("Mã giảm giá đã hết lượt sử dụng.");
//        }
//
//        // Lưu lịch sử sử dụng mã
//        CouponUsage usage = new CouponUsage();
//        usage.setCoupon(coupon);
//        usage.setUser(userRepository.findById(userId).orElseThrow(() ->
//                new IllegalArgumentException("Người dùng không tồn tại.")
//        ));
//        usage.setUsedAt(new Date());
//        couponUsageRepository.save(usage);
//
//        // Giảm đi số lượng usageLimit của mã giảm giá
//        coupon.setUsageLimit(coupon.getUsageLimit() - 1);
//
//        // Lưu lại mã giảm giá đã được cập nhật
//        couponRepository.save(coupon);
//
//        // Áp dụng giảm giá (logic tính toán giảm giá tùy thuộc vào đơn hàng)
//    }

    // Kiểm tra xem mã giảm giá đã được người dùng sử dụng chưa
    public boolean existsByCouponIdAndUserId(int couponId, int userId) {
        return couponUsageRepository.existsByCouponIdAndUserId(couponId, userId);
    }

    // Lưu lịch sử sử dụng mã
    @Override
    public void saveCouponUsage(Coupon coupon, int userId) {
        CouponUsage usage = new CouponUsage();
        usage.setCoupon(coupon);
        usage.setUser(userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("Người dùng không tồn tại.")
        ));
        usage.setUsedAt(new Date());
        couponUsageRepository.save(usage);
    }

}
