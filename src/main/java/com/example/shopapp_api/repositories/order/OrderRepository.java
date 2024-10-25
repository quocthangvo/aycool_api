package com.example.shopapp_api.repositories.order;

import com.example.shopapp_api.entities.orders.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    // Phương thức tìm đơn hàng theo userId
    List<Order> findByUserId(int userId);

}
