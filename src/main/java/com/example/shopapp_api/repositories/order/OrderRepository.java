package com.example.shopapp_api.repositories.order;

import com.example.shopapp_api.entities.orders.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    // Phương thức tìm đơn hàng theo userId
    List<Order> findByUserId(int userId);

//    int countByOrderDate(LocalDate orderDate);

    @Query("SELECT o.orderCode FROM Order o WHERE o.orderCode LIKE :datePrefix% ORDER BY o.createdAt DESC")
    String getLastOrderCodeByDate(@Param("datePrefix") String datePrefix);
}
