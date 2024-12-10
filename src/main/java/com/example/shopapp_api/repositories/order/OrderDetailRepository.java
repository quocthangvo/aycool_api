package com.example.shopapp_api.repositories.order;

import com.example.shopapp_api.entities.orders.Order;
import com.example.shopapp_api.entities.orders.OrderDetail;
import com.example.shopapp_api.entities.products.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    //    List<OrderDetail> findByOrderDetailById(Integer orderId);
    //thay vì viết các câu lệnh SQL thì dùng hàm
    List<OrderDetail> findByOrderId(int orderId);

    OrderDetail findByOrderAndProductDetail(Order order, ProductDetail productDetail);

    // Kiểm tra sản phẩm có trong đơn hàng hay không
    boolean existsByOrderIdAndProductDetail_Id(Long orderId, Long productDetailId);
}
