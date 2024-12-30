package com.example.shopapp_api.services.Impl.order;

import com.example.shopapp_api.dtos.requests.order.OrderDTO;
import com.example.shopapp_api.dtos.requests.order.OrderStatusDTO;
import com.example.shopapp_api.dtos.responses.order.OrderResponse;
import com.example.shopapp_api.dtos.responses.order.StatusResponse;
import com.example.shopapp_api.entities.orders.Order;
import com.example.shopapp_api.entities.orders.status.OrderStatus;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderService {
    OrderResponse createOrder(OrderDTO orderDTO) throws DataNotFoundException;

    OrderResponse getOrderById(int id) throws DataNotFoundException;

    List<OrderResponse> findByUserId(int userId, String status) throws DataNotFoundException;

    void deleteOrder(int id);

    StatusResponse updateOrder(int orderId, OrderStatusDTO orderStatusDTO) throws DataNotFoundException;

    Page<OrderResponse> getAllOrders(PageRequest pageRequest);

    Page<OrderResponse> getAllOrderss(String orderCode, List<OrderStatus> status, LocalDateTime orderDate, Pageable pageable);

    Order applyCouponToOrder(OrderDTO orderDTO);

   
}
