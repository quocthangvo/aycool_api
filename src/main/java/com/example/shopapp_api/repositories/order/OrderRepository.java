package com.example.shopapp_api.repositories.order;

import com.example.shopapp_api.dtos.requests.order.OrderStatsDTO;
import com.example.shopapp_api.entities.orders.Order;
import com.example.shopapp_api.entities.orders.status.OrderStatus;
import com.example.shopapp_api.entities.orders.status.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    // Phương thức tìm đơn hàng theo userId
    List<Order> findByUserId(int userId);

//    int countByOrderDate(LocalDate orderDate);
//
//    @Query("SELECT o.orderCode FROM Order o WHERE o.orderCode LIKE :datePrefix% ORDER BY o.createdAt DESC")
//    String getLastOrderCodeByDate(@Param("datePrefix") String datePrefix);

    // Phương thức truy vấn với các điều kiện lọc theo orderCode, status và orderDate
    @Query("SELECT o FROM Order o WHERE " +
            "(:orderCode IS NULL OR o.orderCode LIKE %:orderCode%) AND " +
            "(:status IS NULL OR o.status IN :status) AND " +
            "(:OrderDate IS NULL OR FUNCTION('DATE', o.OrderDate) = FUNCTION('DATE', :OrderDate))")
    // Only compare date part
    Page<Order> filterOrders(@Param("orderCode") String orderCode,
                             @Param("status") List<OrderStatus> status,  // List to filter by multiple statuses
                             @Param("OrderDate") LocalDateTime OrderDate,
                             Pageable pageable);

//    List<Order> findByUserIdAndStatus(int userId, OrderStatus status);

    // Tìm tất cả đơn hàng theo userId và sắp xếp theo createAt giảm dần
    List<Order> findByUserIdOrderByCreatedAtDesc(int userId);

    // Tìm tất cả đơn hàng theo userId và trạng thái, sắp xếp theo createAt giảm dần
    List<Order> findByUserIdAndStatusOrderByCreatedAtDesc(int userId, OrderStatus status);

    @Query("SELECT o FROM Order o JOIN o.orderDetails od WHERE o.user.id = :userId AND o.status = :status AND od.productDetail.id = :productDetailId")
    Optional<Order> findByUserIdAndStatusAndOrderDetailsProductDetailId(
            @Param("userId") int userId,
            @Param("status") OrderStatus status,
            @Param("productDetailId") int productDetailId);


    Optional<Order> findById(int id);

    //tong tien don hang
    //    @Query("SELECT SUM(o.totalMoney) FROM Order o WHERE o.active = true")
//    Double calculateTotalMoneyForAllOrders();

    //tong tien donhang đã thanh toán
    @Query("SELECT SUM(o.totalMoney) FROM Order o WHERE o.paymentStatus = :paymentStatus")
    Double calculateTotalMoneyByPaymentStatus(@Param("paymentStatus") PaymentStatus paymentStatus);

    // tong tien co tong tien da giam
    List<Order> findAllByPaymentStatus(PaymentStatus paymentStatus);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.paymentStatus = :paymentStatus")
    Long countByPaymentStatus(@Param("paymentStatus") PaymentStatus paymentStatus);


    // Tong don hang
    @Query("SELECT COUNT(o) FROM Order o WHERE DATE(o.OrderDate) = CURRENT_DATE")
    Long countTodayOrders();

    //tong tien don hang trong ngày
    @Query("SELECT COALESCE(SUM(CASE WHEN o.totalMoneyAfterDiscount IS NOT NULL THEN " +
            "o.totalMoneyAfterDiscount ELSE o.totalMoney END), 0) " +
            "FROM Order o " +
            "WHERE o.paymentStatus = 'PAID' " +
            "AND FUNCTION('DATE', o.OrderDate) = CURRENT_DATE")
    Double calculateTotalPaidOrdersToday();


}
