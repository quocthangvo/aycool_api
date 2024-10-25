package com.example.shopapp_api.services.Serv.order;

import com.example.shopapp_api.dtos.requests.order.OrderDTO;
import com.example.shopapp_api.dtos.requests.order.OrderStatusDTO;
import com.example.shopapp_api.dtos.responses.order.OrderResponse;
import com.example.shopapp_api.entities.orders.Address;
import com.example.shopapp_api.entities.orders.Order;
import com.example.shopapp_api.entities.orders.OrderStatus;
import com.example.shopapp_api.entities.users.User;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.repositories.user.AddressRepository;
import com.example.shopapp_api.repositories.order.OrderRepository;
import com.example.shopapp_api.repositories.user.UserRepository;
import com.example.shopapp_api.services.Impl.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    @Override
    public OrderResponse createOrder(OrderDTO orderDTO) throws DataNotFoundException {
        User user = userRepository
                .findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Người dùng không tồn tại với id " + orderDTO.getUserId()));
        // Lấy thông tin địa chỉ
        Address address = addressRepository
                .findById(orderDTO.getAddressId())
                .orElseThrow(() -> new DataNotFoundException("Địa chỉ không tồn tại với id " + orderDTO.getAddressId()));

        // Kiểm tra địa chỉ có thuộc về người dùng hay không
        if (address.getUser().getId() != user.getId()) {
            throw new DataNotFoundException("Địa chỉ không thuộc về người dùng này");
        }
//       convert orderDTO -> order
        // dùng thư viện model mapper
        // ánh xạ từ orderDTO -> order skip qua id
//        modelMapper.typeMap(OrderDTO.class, Order.class)
//                .addMappings(mapper -> mapper.skip(Order::setId));
//        cập nhật các trươnng đơn hàng từ orderDTO
        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setAddress(address);

        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        //kt ngày giao hàng k phải hôm nay
        LocalDateTime shippingDate = orderDTO.getShippingDate() == null ? LocalDateTime.now() : orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDateTime.now())) {
            throw new DataNotFoundException("Ngày giao hàng không phải hôm nay");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);
        // trả về
        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    public OrderResponse getOrderById(int id) throws DataNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm với id: " + id));
        // Kiểm tra và gán trạng thái nếu cần thiết
        if (order.getStatus() == null) {
            throw new DataNotFoundException("Trạng thái đơn hàng không hợp lệ.");
        }
        return modelMapper.map(order, OrderResponse.class);
    }


    @Override
    public List<OrderResponse> findByUserId(int userId) throws DataNotFoundException {
        // Lấy danh sách Order từ repository
        List<Order> orders = orderRepository.findByUserId(userId);

        // Kiểm tra nếu không có đơn hàng
        if (orders.isEmpty()) {
            throw new DataNotFoundException("Không có đơn hàng nào cho người dùng với ID " + userId);
        }

//        return orders; // Trả về danh sách đơn hàng

        // Chuyển đổi danh sách Order sang danh sách OrderResponse
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderResponse.class))
                .collect(Collectors.toList()); // Trả về danh sách đơn hàng
    }

    @Override
    public void deleteOrder(int id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + id));

        // Kiểm tra xem trạng thái của đơn hàng có phải là PENDING không
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Chỉ có thể hủy đơn hàng khi ở trạng thái Chờ xử lý.");
        }
        //ko xóa cứng
        // Sử dụng isEmpty() thay cho !isPresent()
        order.setActive(false);
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

//    @Override
//    public OrderResponse updateOrder(int orderId, OrderDTO orderDTO) throws DataNotFoundException {
//        Order existingOrder = orderRepository.findById(orderId)
//                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy user id:" + orderId));
//        User existingUser = userRepository.findById(orderDTO.getUserId())
//                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy user id:" + orderDTO.getUserId()));
//
//        modelMapper.typeMap(OrderDTO.class, Order.class)
//                .addMappings(mapper -> mapper.skip(Order::setId));
//        //update data
//        modelMapper.map(orderDTO, existingOrder);
//        existingOrder.setUser(existingUser);
////        return orderRepository.save(existingOrder);
//        // Lưu đơn hàng đã cập nhật
//        Order updatedOrder = orderRepository.save(existingOrder);
//        // Chuyển đổi và trả về OrderResponse
//        return modelMapper.map(updatedOrder, OrderResponse.class);
//    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public OrderResponse updateOrder(int orderId, OrderStatusDTO orderStatusDTO) throws DataNotFoundException {
        // Tìm kiếm đơn hàng theo orderId
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng với ID: " + orderId));
        // Kiểm tra trạng thái mới từ orderStatusDTO
        if (orderStatusDTO.getStatus() != null) {
            // Kiểm tra trạng thái hiện tại và trạng thái mới
            OrderStatus currentStatus = existingOrder.getStatus();
            OrderStatus newStatus = orderStatusDTO.getStatus();

            // Kiểm tra thứ tự trạng thái hợp lệ
            if (!isValidTransition(currentStatus, newStatus)) {
                throw new IllegalArgumentException("Không thể chuyển trạng thái từ " + currentStatus + " sang " + newStatus);
            }

            existingOrder.setStatus(newStatus);
        }

        // Lưu đơn hàng đã cập nhật
        Order updatedOrder = orderRepository.save(existingOrder);

        // Chuyển đổi và trả về OrderResponse
        return modelMapper.map(updatedOrder, OrderResponse.class);
    }

    // Phương thức kiểm tra thứ tự trạng thái
    private boolean isValidTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        return switch (currentStatus) {
            case PENDING -> newStatus == OrderStatus.PROCESSING || newStatus == OrderStatus.CANCELLED;
            case PROCESSING -> newStatus == OrderStatus.SHIPPED;
            case SHIPPED -> newStatus == OrderStatus.DELIVERED;
            case DELIVERED -> false; // Không thể thay đổi trạng thái sau khi đã giao hàng
            case CANCELLED -> false; // Không thể thay đổi trạng thái sau khi đã hủy
            default -> false;
        };
    }

}
