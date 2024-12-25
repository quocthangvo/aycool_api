package com.example.shopapp_api.services.Serv.coupon;

import com.example.shopapp_api.dtos.requests.coupon.CouponDTO;
import com.example.shopapp_api.entities.coupon.Coupon;
import com.example.shopapp_api.entities.coupon.CouponUsage;
import com.example.shopapp_api.entities.coupon.DiscountType;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.repositories.coupon.CouponRepository;
import com.example.shopapp_api.repositories.coupon.CouponUsageRepository;
import com.example.shopapp_api.repositories.user.UserRepository;
import com.example.shopapp_api.services.Impl.coupon.ICouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService implements ICouponService {
    private final CouponRepository couponRepository;
    private final CouponUsageRepository couponUsageRepository;
    private final UserRepository userRepository;

    @Override
    public Coupon createCoupon(CouponDTO couponDTO) throws DataNotFoundException {
        // Kiểm tra nếu mã giảm giá đã tồn tại
        if (couponRepository.findByCode(couponDTO.getCode()).isPresent()) {
            throw new DataNotFoundException("Mã giảm giá đã tồn tại.");
        }

        // Tạo thực thể Coupon từ dữ liệu của DTO
        Coupon coupon = new Coupon();
        coupon.setCode(couponDTO.getCode());
        coupon.setDescription(couponDTO.getDescription());
        coupon.setDiscountType(couponDTO.getDiscountType());
        coupon.setMinOrderValue(couponDTO.getMinOrderValue());
        coupon.setStartDate(couponDTO.getStartDate());
        coupon.setEndDate(couponDTO.getEndDate());
        coupon.setUsageLimit(couponDTO.getUsageLimit());
        coupon.setStatus(true);

        // Kiểm tra loại giảm giá và xử lý giá trị giảm giá
        if (couponDTO.getDiscountType() == DiscountType.PERCENT) {
            // Nếu là phần trăm, giá trị discountValue sẽ là phần trăm giảm giá
            if (couponDTO.getDiscountValue() > 100) {
                throw new DataNotFoundException("Giảm giá phần trăm không thể vượt quá 100%");
            }
            coupon.setDiscountValue(couponDTO.getDiscountValue());
        } else if (couponDTO.getDiscountType() == DiscountType.FIXED_AMOUNT) {
            // Nếu là tiền cố định, giá trị discountValue là số tiền giảm
            if (couponDTO.getDiscountValue() > couponDTO.getMinOrderValue()) {
                throw new DataNotFoundException("Giảm giá tiền cố định không thể vượt quá giá trị đơn hàng tối thiểu");
            }
            coupon.setDiscountValue(couponDTO.getDiscountValue());
        }

        // Lưu mã giảm giá vào cơ sở dữ liệu
        return couponRepository.save(coupon);
    }


    @Override
    public Coupon getCouponById(int id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mã giảm giá không tồn tại."));
    }


    @Override
    public List<Coupon> getAllCoupons() {
        List<Coupon> coupons = couponRepository.findAll(Sort.by(Sort.Order.desc("createdAt")));

        // Chuyển đổi DiscountType thành chuỗi mô tả
        for (Coupon coupon : coupons) {
            String discountDescription = coupon.getDiscountType().getDiscountType(); // Lấy giá trị từ enum
            // Gán giá trị mô tả vào đối tượng Coupon hoặc xử lý ở đây
        }

        return coupons;
    }


    @Override
    public void deleteCoupon(int id) {
        if (!couponRepository.existsById(id)) {
            throw new RuntimeException("Mã giảm giá không tồn tại.");
        }
        couponRepository.deleteById(id);
    }

    @Override
    public Coupon updateCoupon(int couponId, CouponDTO couponDTO) throws DataNotFoundException {
        // Kiểm tra nếu mã giảm giá có tồn tại không
        Coupon existingCoupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new DataNotFoundException("Mã giảm giá không tồn tại"));

        // Kiểm tra nếu loại giảm giá thay đổi và loại giảm giá mới là phần trăm
        if (!existingCoupon.getDiscountType().equals(couponDTO.getDiscountType())) {
            if (couponDTO.getDiscountType() == DiscountType.PERCENT) {
                // Kiểm tra nếu giá trị phần trăm vượt quá 100%
                if (couponDTO.getDiscountValue() > 100) {
                    throw new DataNotFoundException("Giảm giá phần trăm không thể vượt quá 100%");
                }
            } else if (couponDTO.getDiscountType() == DiscountType.FIXED_AMOUNT) {
                // Nếu loại giảm giá là tiền cố định, kiểm tra nếu giá trị giảm không vượt quá minOrderValue
                if (couponDTO.getDiscountValue() > couponDTO.getMinOrderValue()) {
                    throw new DataNotFoundException("Giảm giá tiền cố định không thể vượt quá giá trị đơn hàng tối thiểu");
                }
            }
        }

        // Cập nhật các thông tin từ DTO
        existingCoupon.setCode(couponDTO.getCode());
        existingCoupon.setDescription(couponDTO.getDescription());
        existingCoupon.setDiscountType(couponDTO.getDiscountType());
        existingCoupon.setDiscountValue(couponDTO.getDiscountValue());
        existingCoupon.setMinOrderValue(couponDTO.getMinOrderValue());
        existingCoupon.setStartDate(couponDTO.getStartDate());
        existingCoupon.setEndDate(couponDTO.getEndDate());
        existingCoupon.setUsageLimit(couponDTO.getUsageLimit());
        existingCoupon.setStatus(couponDTO.isStatus());  // Cập nhật trạng thái


        // Kiểm tra nếu ngày hiện tại đã vượt quá ngày endDate, nếu có thì tự động chuyển trạng thái thành false
        if (existingCoupon.getEndDate().isBefore(LocalDate.now())) {
            existingCoupon.setStatus(false);
        } else {
            existingCoupon.setStatus(couponDTO.isStatus());  // Cập nhật trạng thái nếu ngày chưa hết hạn
        }
        // Lưu cập nhật vào cơ sở dữ liệu
        return couponRepository.save(existingCoupon);
    }


    @Override
    public List<Coupon> getAllCouponsStatus() {
        // Lọc các mã giảm giá có trạng thái là true
        return couponRepository.findAll().stream()
                .filter(Coupon::isStatus) // Chỉ lấy các mã giảm giá có status = true
                .collect(Collectors.toList());
    }


    // Áp dụng mã giảm giá
//    @Override
//    public void applyCoupon(String code, int userId) {
//        // Tìm mã giảm giá
//        Coupon coupon = couponRepository.findByCode(code)
//                .orElseThrow(() -> new IllegalArgumentException("Mã giảm giá không tồn tại."));
//
//        // Kiểm tra thời hạn mã giảm giá
//        LocalDate now = new Date();
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
//        usage.setUser(userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại.")));
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
}
