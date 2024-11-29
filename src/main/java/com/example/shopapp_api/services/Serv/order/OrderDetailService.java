package com.example.shopapp_api.services.Serv.order;

import com.example.shopapp_api.dtos.requests.order.OrderDetailDTO;
import com.example.shopapp_api.dtos.requests.order.UpdateOrderDetailDTO;
import com.example.shopapp_api.dtos.responses.order.AddressResponse;
import com.example.shopapp_api.dtos.responses.order.OrderDetailResponse;
import com.example.shopapp_api.dtos.responses.order.StatusResponse;
import com.example.shopapp_api.dtos.responses.order.TotalResponse;
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
import java.util.stream.Collectors;

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

    public TotalResponse getTotal(int orderId) {
        // Lấy tất cả các chi tiết đơn hàng theo orderId
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);

        // Chuyển từng OrderDetail thành OrderDetailResponse
        List<OrderDetailResponse> detailResponses = orderDetails.stream()
                .map(OrderDetailResponse::formOrderDetail)
                .collect(Collectors.toList());

        // Lấy tổng tiền từ Order (giả sử đơn hàng không rỗng)
        Float totalMoney = !orderDetails.isEmpty() ? orderDetails.get(0).getOrder().getTotalMoney() : 0;

        // Lấy thông tin đơn hàng
        Order order = orderDetails.isEmpty() ? null : orderDetails.get(0).getOrder();

        // Lấy thông tin địa chỉ
        AddressResponse addressResponse = null;
        if (order != null && order.getAddress() != null) {
            addressResponse = AddressResponse.formAddress(order.getAddress());
        }

        // Lấy thông tin trạng thái
        StatusResponse statusResponse = (order != null)
                ? StatusResponse.formStatus(order)
                : null;


        // Tạo phản hồi gói
        return new TotalResponse(detailResponses, totalMoney, addressResponse, statusResponse);
    }

}
