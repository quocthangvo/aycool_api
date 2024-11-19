package com.example.shopapp_api.services.Serv.order;

import com.example.shopapp_api.dtos.requests.order.OrderDetailDTO;
import com.example.shopapp_api.dtos.requests.order.UpdateOrderDetailDTO;
import com.example.shopapp_api.dtos.responses.order.OrderDetailResponse;
import com.example.shopapp_api.entities.orders.Order;
import com.example.shopapp_api.entities.orders.OrderDetail;
import com.example.shopapp_api.entities.products.Product;
import com.example.shopapp_api.entities.products.ProductDetail;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.repositories.order.OrderDetailRepository;
import com.example.shopapp_api.repositories.order.OrderRepository;
import com.example.shopapp_api.repositories.product.ProductDetailRepository;
import com.example.shopapp_api.repositories.product.ProductRepository;
import com.example.shopapp_api.services.Impl.order.IOrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;

    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng có id " + orderDetailDTO.getOrderId()));
        ProductDetail productDetail = productDetailRepository.findById(orderDetailDTO.getProductDetailId())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm có id " + orderDetailDTO.getProductDetailId()));
        OrderDetail existingOrderDetail = orderDetailRepository.findByOrderAndProductDetail(order, productDetail);

        if (existingOrderDetail != null) {
            // If it exists, increase the quantity and update the total money
            existingOrderDetail.setQuantity(existingOrderDetail.getQuantity() + orderDetailDTO.getQuantity());
            existingOrderDetail.setTotalMoney(existingOrderDetail.getTotalMoney() + orderDetailDTO.getTotalMoney());

            // Save the updated OrderDetail
            return orderDetailRepository.save(existingOrderDetail);
        } else {
            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .productDetail(productDetail)
                    .quantity(orderDetailDTO.getQuantity())
//                    .price(orderDetailDTO.getPrice())
                    .totalMoney(orderDetailDTO.getTotalMoney())
                    .build();
            return orderDetailRepository.save(orderDetail);
        }
    }

    @Override
    public OrderDetailResponse getOrderDetailById(int id) {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết đơn hàng với id: " + id));
        return OrderDetailResponse.formOrderDetail(orderDetail);
    }

    @Override
    public List<OrderDetail> findByOrderId(int orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }

    @Override
    public void deleteOrderDetail(int id) {
        getOrderDetailById(id);
        //xóa cứng
        orderDetailRepository.deleteById(id);
    }

    @Override
    public OrderDetailResponse updateOrderDetail(int orderId, UpdateOrderDetailDTO updateOrderDetailDTO) throws DataNotFoundException {
        //kiem tra order detail có ton tai k
//        OrderDetail existingOrderDtail = getOrderDetailById(orderId);
        OrderDetail existingOrderDetail = orderDetailRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy chi tiết đơn hàng có id" + orderId));
//        Order existingOrder = orderRepository.findById(updateOrderDetailDTO.ge())
//                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng có id " + orderDetailDTO.getOrderId()));
//        ProductDetail existingProductDetail = productDetailRepository.findById(orderDetailDTO.getProductDetailId())
//                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm có id " + orderDetailDTO.getProductDetailId()));

//        existingOrderDetail.setPrice(updateOrderDetailDTO.getPrice());
        existingOrderDetail.setQuantity(updateOrderDetailDTO.getQuantity());
        existingOrderDetail.setTotalMoney(updateOrderDetailDTO.getTotalMoney());

        existingOrderDetail = orderDetailRepository.save(existingOrderDetail);
        return OrderDetailResponse.formOrderDetail(existingOrderDetail);
    }
}
