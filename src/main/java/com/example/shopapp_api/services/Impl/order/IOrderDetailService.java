package com.example.shopapp_api.services.Impl.order;

import com.example.shopapp_api.dtos.requests.order.OrderDetailDTO;
import com.example.shopapp_api.dtos.requests.order.UpdateOrderDetailDTO;
import com.example.shopapp_api.dtos.responses.order.OrderDetailResponse;
import com.example.shopapp_api.dtos.responses.order.TotalResponse;
import com.example.shopapp_api.entities.orders.OrderDetail;
import com.example.shopapp_api.exceptions.DataNotFoundException;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFoundException;

    OrderDetailResponse getOrderDetailById(int id) throws DataNotFoundException;

    List<OrderDetail> findByOrderId(int orderId) throws DataNotFoundException;

    void deleteOrderDetail(int id);

    OrderDetailResponse updateOrderDetail(int orderId, UpdateOrderDetailDTO updateOrderDetailDTO) throws DataNotFoundException;

    TotalResponse getTotal(int orderId);

    OrderDetailResponse getTopSellingProduct();

    List<OrderDetailResponse> getTopSellingProducts();

    OrderDetailResponse getLowSellingProduct(); //sp bán chậm

    List<OrderDetailResponse> getLowSellingProducts();
}
