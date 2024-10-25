package com.example.shopapp_api.services.Impl.order;

import com.example.shopapp_api.dtos.requests.order.OrderDTO;
import com.example.shopapp_api.dtos.requests.order.OrderStatusDTO;
import com.example.shopapp_api.dtos.responses.order.OrderResponse;
import com.example.shopapp_api.exceptions.DataNotFoundException;

import java.util.List;

public interface IOrderService {
    OrderResponse createOrder(OrderDTO orderDTO) throws DataNotFoundException;

    OrderResponse getOrderById(int id) throws DataNotFoundException;

    List<OrderResponse> findByUserId(int userId) throws DataNotFoundException;

    void deleteOrder(int id);

    OrderResponse updateOrder(int orderId, OrderStatusDTO orderStatusDTO) throws DataNotFoundException;
}
