package com.example.shopapp_api.services.Serv.order;

import com.example.shopapp_api.dtos.requests.order.OrderDTO;
import com.example.shopapp_api.dtos.requests.order.OrderStatusDTO;
import com.example.shopapp_api.dtos.responses.order.OrderResponse;
import com.example.shopapp_api.dtos.responses.order.StatusResponse;
import com.example.shopapp_api.entities.cart.Cart;
import com.example.shopapp_api.entities.cart.CartItem;
import com.example.shopapp_api.entities.orders.*;
import com.example.shopapp_api.entities.orders.status.OrderStatus;
import com.example.shopapp_api.entities.orders.status.PaymentMethod;
import com.example.shopapp_api.entities.orders.status.PaymentStatus;
import com.example.shopapp_api.entities.prices.Price;
import com.example.shopapp_api.entities.products.ProductDetail;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.repositories.cart.CartRepository;
import com.example.shopapp_api.repositories.order.OrderDetailRepository;
import com.example.shopapp_api.repositories.price.PriceRepository;
import com.example.shopapp_api.repositories.product.ProductDetailRepository;
import com.example.shopapp_api.repositories.user.AddressRepository;
import com.example.shopapp_api.repositories.order.OrderRepository;
import com.example.shopapp_api.repositories.user.UserRepository;
import com.example.shopapp_api.services.Impl.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ProductDetailRepository productDetailRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PriceRepository priceRepository;
    private final ModelMapper modelMapper;
    private final CartRepository cartRepository;

//    @Override
//    public OrderResponse createOrder(OrderDTO orderDTO) throws DataNotFoundException {
//        User user = userRepository
//                .findById(orderDTO.getUserId())
//                .orElseThrow(() -> new DataNotFoundException("Người dùng không tồn tại với id " + orderDTO.getUserId()));
//        // Lấy thông tin địa chỉ
//        Address address = addressRepository
//                .findById(orderDTO.getAddressId())
//                .orElseThrow(() -> new DataNotFoundException("Địa chỉ không tồn tại với id " + orderDTO.getAddressId()));
//
//        // Kiểm tra địa chỉ có thuộc về người dùng hay không
//        if (address.getUser().getId() != user.getId()) {
//            throw new DataNotFoundException("Địa chỉ không thuộc về người dùng này");
//        }
////       convert orderDTO -> order
//        // dùng thư viện model mapper
//        // ánh xạ từ orderDTO -> order skip qua id
////        modelMapper.typeMap(OrderDTO.class, Order.class)
////                .addMappings(mapper -> mapper.skip(Order::setId));
////        cập nhật các trươnng đơn hàng từ orderDTO
//
//        Order order = new Order();
//        modelMapper.map(orderDTO, order);
//        order.setUser(user);
//        order.setAddress(address);
//
//        order.setOrderDate(LocalDateTime.now());
//        order.setStatus(OrderStatus.PENDING);
//        //kt ngày giao hàng k phải hôm nay
//        LocalDateTime shippingDate = orderDTO.getShippingDate() == null ? LocalDateTime.now() : orderDTO.getShippingDate();
//        if (shippingDate.isBefore(LocalDateTime.now())) {
//            throw new DataNotFoundException("Ngày giao hàng không phải hôm nay");
//        }
//        order.setShippingDate(shippingDate);
//        order.setActive(true);
//
//        String orderCode = generateOrderCode();
//        order.setOrderCode(orderCode);
//
//        orderRepository.save(order);
//
//        // Tính tổng tiền đơn hàng
//        float totalMoney = 0;
//
//        //tạo danh sách chi tiết đơn hàng
//        List<OrderDetail> orderDetails = new ArrayList<>();
//        for (OrderDetailDTO orderDetailDTO : orderDTO.getOrderDetails()) {
//            ProductDetail productDetail = productDetailRepository.findById(orderDetailDTO.getProductDetailId())
//                    .orElseThrow(() -> new DataNotFoundException(
//                            "Không tìm thấy chi tiết đơn hàng với id: " + orderDetailDTO.getProductDetailId()));
//
//            // Lấy giá của sản phẩm (giá bán hoặc giá khuyến mãi)
//            Price price = priceRepository.findTopByProductDetailIdOrderByCreatedAtDesc(productDetail.getId())
//                    .orElseThrow(() -> new DataNotFoundException(
//                            "Không tìm thấy giá cho sản phẩm với id: " + productDetail.getId()));
//            System.out.print("gia cuoi cung");
//            System.out.println(price);
//            // Lấy giá bán hoặc giá khuyến mãi (nếu có)
//            Float productPrice = (price.getPromotionPrice() != null && price.getPromotionPrice() > 0)
//                    ? price.getPromotionPrice() // Nếu có giá khuyến mãi thì dùng giá khuyến mãi
//                    : price.getSellingPrice(); // Ngược lại, dùng giá bán
//
//            // Tính tiền cho chi tiết sản phẩm và cộng vào tổng tiền
//            Float detailTotal = productPrice * orderDetailDTO.getQuantity();
//            totalMoney += detailTotal;
//
//            OrderDetail orderDetail = OrderDetail.builder()
//                    .order(order)
//                    .productDetail(productDetail)
//                    .quantity(orderDetailDTO.getQuantity())
//                    .totalMoney(detailTotal)
//                    .build();
//            orderDetails.add(orderDetailRepository.save(orderDetail));
//
//            // Cộng vào tổng tiền của đơn hàng
////            totalMoney += detailTotal;
//        }
//
//        // trả về
//        // Cập nhật tổng tiền vào đơn hàng
//        order.setOrderDetails(orderDetails);
//        order.setTotalMoney(totalMoney);
//        orderRepository.save(order);
//        return OrderResponse.formOrder(order);
////        return modelMapper.map(order, OrderResponse.class);
//    }

    @Override
    public Page<OrderResponse> getAllOrders(PageRequest pageRequest) {
        return orderRepository.findAll(pageRequest).map(OrderResponse::formOrder);
        // tham chiếu đến phương thức response :: thay vì dùng (product -> )
    }

    @Override
    public OrderResponse getOrderById(int id) throws DataNotFoundException {
        // Lấy order từ database hoặc throw exception
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng với id: " + id));

        // Kiểm tra trạng thái đơn hàng
        Optional.ofNullable(order.getStatus())
                .orElseThrow(() -> new DataNotFoundException("Trạng thái đơn hàng không hợp lệ."));

        // Trả về OrderResponse
        return OrderResponse.formOrder(order);
    }


    @Override
    public List<OrderResponse> findByUserId(int userId, String status) throws DataNotFoundException {
        // Kiểm tra nếu trạng thái được cung cấp, nếu có thì lọc theo trạng thái đó
        List<Order> orders;

        // Chuyển đổi String status thành OrderStatus enum
//        OrderStatus orderStatus = null;
//        if (status != null && !status.isEmpty()) {
//            try {
//                orderStatus = OrderStatus.valueOf(status.toUpperCase()); // Chuyển đổi status sang Enum
//            } catch (IllegalArgumentException e) {
//                throw new DataNotFoundException("Trạng thái đơn hàng không hợp lệ: " + status);
//            }
//        }
        if (status != null && !status.isEmpty()) {
            // Nếu trạng thái được cung cấp, lọc theo trạng thái
            orders = orderRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, OrderStatus.valueOf(status));
        } else {
            // Nếu không có trạng thái, lấy tất cả đơn hàng của người dùng
            orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        }


        // Nếu không có đơn hàng nào, trả về danh sách rỗng thay vì null
        if (orders.isEmpty()) {
            return Collections.emptyList(); // Trả về danh sách rỗng
        }

        // Chuyển đổi danh sách Order sang danh sách OrderResponse
        return orders.stream()
                .map(OrderResponse::formOrder)
                .collect(Collectors.toList());
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

//    @Override
//    @PreAuthorize("hasRole('ADMIN')")
//    public OrderResponse updateOrder(int orderId, OrderStatusDTO orderStatusDTO) throws DataNotFoundException {
//        // Tìm kiếm đơn hàng theo orderId
//        Order existingOrder = orderRepository.findById(orderId)
//                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng với ID: " + orderId));
//        // Kiểm tra trạng thái mới từ orderStatusDTO
//        if (orderStatusDTO.getStatus() != null) {
//            // Kiểm tra trạng thái hiện tại và trạng thái mới
//            OrderStatus currentStatus = existingOrder.getStatus();
//            OrderStatus newStatus = orderStatusDTO.getStatus();
//
//            // Kiểm tra thứ tự trạng thái hợp lệ
//            if (!isValidTransition(currentStatus, newStatus)) {
//                throw new IllegalArgumentException("Không thể chuyển trạng thái từ " + currentStatus + " sang " + newStatus);
//            }
//            existingOrder.setStatus(newStatus);
//        }
//
//        // Lưu đơn hàng đã cập nhật
//        Order updatedOrder = orderRepository.save(existingOrder);
//
//        // Chuyển đổi và trả về OrderResponse
//        return modelMapper.map(updatedOrder, OrderResponse.class);
//    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public StatusResponse updateOrder(int orderId, OrderStatusDTO orderStatusDTO) throws DataNotFoundException {
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
//            // Kiểm tra nếu trạng thái mới là "Đã giao hàng" (DELIVERED), thì cập nhật trạng thái thanh toán là "PAID"
//            if (newStatus == OrderStatus.DELIVERED) {
//                existingOrder.setPaymentStatus(PaymentStatus.PAID);
//            }
            // Cập nhật ngày tương ứng với trạng thái mới
            LocalDateTime now = LocalDateTime.now();
            switch (newStatus) {
                case PROCESSING:
                    existingOrder.setProcessingDate(now);
                    break;
                case SHIPPED:
                    existingOrder.setShippingDate(now);
                    break;
                case DELIVERED:
                    existingOrder.setDeliveredDate(now);
                    existingOrder.setPaymentStatus(PaymentStatus.PAID); // Nếu đã giao hàng, cập nhật thanh toán là "Đã thanh toán"
                    break;
                case CANCELLED:
                    existingOrder.setCancelledDate(now);
                    break;
                default:
                    break;
            }
        }

        // Lưu đơn hàng đã cập nhật
        Order updatedOrder = orderRepository.save(existingOrder);

        // Trả về StatusResponse
        return StatusResponse.formStatus(updatedOrder);
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


    // Lưu trữ số đếm cho mỗi ngày, sử dụng ConcurrentHashMap để xử lý đa luồng
    private static final ConcurrentHashMap<String, Integer> orderCounters = new ConcurrentHashMap<>();

    // Phương thức để sinh mã đơn hàng
    private String generateOrderCode() {
        // Lấy ngày hiện tại theo định dạng yyyyMMdd
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());

        Random random = new Random();
        int randomSuffix = random.nextInt(10000);  // Giới hạn từ 0 đến 9999

        // Đảm bảo số ngẫu nhiên có đúng 4 chữ số
        String suffix = String.format("%04d", randomSuffix);

        return "ORDER-" + date + "-" + suffix;
    }


//    @Override
//    public OrderResponse createOrder(OrderDTO orderDTO) throws DataNotFoundException {
//        // Lấy giỏ hàng của người dùng
//        Cart cart = cartRepository.findByUserId(orderDTO.getUserId())
//                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy giỏ hàng của người dùng"));
//
//        // Kiểm tra địa chỉ giao hàng
//        Address address = addressRepository.findById(orderDTO.getAddressId())
//                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy địa chỉ với id: " + orderDTO.getAddressId()));
//
//        // Tạo đơn hàng mới
//        Order order = new Order();
//        order.setUser(cart.getUser());
//        order.setAddress(address);
//        order.setOrderDate(LocalDateTime.now());
//        order.setStatus(OrderStatus.PENDING);
//        order.setActive(true);
//        order.setOrderCode(generateOrderCode());
//        order.setPaymentMethod(orderDTO.getPaymentMethod());
//        order.setShippingDate(orderDTO.getShippingDate());
//        order.setNote(orderDTO.getNote());
//        orderRepository.save(order);
//        //kt ngày giao hàng k phải hôm nay
//        LocalDateTime shippingDate = orderDTO.getShippingDate() == null ? LocalDateTime.now() : orderDTO.getShippingDate();
//        if (shippingDate.isBefore(LocalDateTime.now())) {
//            throw new DataNotFoundException("Ngày giao hàng không phải hôm nay");
//        }
//
//        // Duyệt qua các mục trong giỏ hàng và thêm vào `OrderDetail`
//        float totalMoney = 0;
//        List<OrderDetail> orderDetails = new ArrayList<>();
//
//        for (CartItem cartItem : cart.getItems()) {
//            ProductDetail productDetail = cartItem.getProductDetail();
//
//            // Tính giá sản phẩm (ưu tiên giá khuyến mãi nếu có)
//            Price price = productDetail.getPrices().stream()
//                    .max(Comparator.comparing(Price::getCreatedAt))
//                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy giá cho sản phẩm"));
//
//            float productPrice = (price.getPromotionPrice() != null && price.getPromotionPrice() > 0)
//                    ? price.getPromotionPrice()
//                    : price.getSellingPrice();
//
//            float detailTotal = productPrice * cartItem.getQuantity();
//            totalMoney += detailTotal;
//
//            // Tạo `OrderDetail`
//            OrderDetail orderDetail = new OrderDetail();
//            orderDetail.setOrder(order);
//            orderDetail.setProductDetail(productDetail);
//            orderDetail.setQuantity(cartItem.getQuantity());
//            orderDetail.setTotalMoney(detailTotal);
//            orderDetailRepository.save(orderDetail);
//
//            orderDetails.add(orderDetail);
//        }
//
//        // Cập nhật tổng tiền cho đơn hàng
//        order.setTotalMoney(totalMoney);
//        order.setOrderDetails(orderDetails);
//        orderRepository.save(order);
//
//        // Xóa giỏ hàng sau khi đặt hàng thành công
//        cart.getItems().clear();
//        cartRepository.save(cart);
//
//        // Trả về thông tin đơn hàng
//        return OrderResponse.formOrder(order);
//    }

    @Override
    public OrderResponse createOrder(OrderDTO orderDTO) throws DataNotFoundException {
        // Lấy giỏ hàng của người dùng
        Cart cart = cartRepository.findByUserId(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy giỏ hàng của người dùng"));

        // Kiểm tra địa chỉ giao hàng
        Address address = addressRepository.findById(orderDTO.getAddressId())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy địa chỉ với id: " + orderDTO.getAddressId()));

        // Tạo đơn hàng mới
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setAddress(address);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setActive(true);
        order.setOrderCode(generateOrderCode());
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setShippingDate(orderDTO.getShippingDate());
        order.setNote(orderDTO.getNote());

        // Kiểm tra phương thức thanh toán và cập nhật paymentStatus
        if (PaymentMethod.COD.equals(orderDTO.getPaymentMethod())) {
            order.setPaymentStatus(PaymentStatus.NOPAYMENT);  // Nếu phương thức là COD, trạng thái thanh toán là NOPAYMENT
        } else if (PaymentMethod.ONLINE_PAYMENT.equals(orderDTO.getPaymentMethod())) {
            order.setPaymentStatus(PaymentStatus.PAID);  // Nếu phương thức là ONLINE, trạng thái thanh toán là PAID
        } else {
            throw new DataNotFoundException("Phương thức thanh toán không hợp lệ");
        }

        orderRepository.save(order);

        // Kiểm tra ngày giao hàng
        LocalDateTime shippingDate = orderDTO.getShippingDate() == null ? LocalDateTime.now() : orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDateTime.now())) {
            throw new DataNotFoundException("Ngày giao hàng không phải hôm nay");
        }

        // Duyệt qua các mục trong giỏ hàng được chọn và thêm vào `OrderDetail`
        float totalMoney = 0;
        List<OrderDetail> orderDetails = new ArrayList<>();

        // Lọc các CartItem theo cartItemId từ selectedItems
        for (CartItem cartItem : cart.getItems()) {
            if (orderDTO.getSelectedItems().contains(cartItem.getId())) {  // Kiểm tra nếu cartItemId có trong selectedItems
                ProductDetail productDetail = cartItem.getProductDetail();

                // Tính giá sản phẩm (ưu tiên giá khuyến mãi nếu có)
                Price price = productDetail.getPrices().stream()
                        .max(Comparator.comparing(Price::getCreatedAt))
                        .orElseThrow(() -> new DataNotFoundException("Không tìm thấy giá cho sản phẩm"));

                float productPrice = (price.getPromotionPrice() != null && price.getPromotionPrice() > 0)
                        ? price.getPromotionPrice()
                        : price.getSellingPrice();

                float detailTotal = productPrice * cartItem.getQuantity();
                totalMoney += detailTotal;

                // Tạo `OrderDetail`
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setProductDetail(productDetail);
                orderDetail.setQuantity(cartItem.getQuantity());
                orderDetail.setTotalMoney(detailTotal);
                orderDetailRepository.save(orderDetail);

                orderDetails.add(orderDetail);
            }
        }

        // Cập nhật tổng tiền cho đơn hàng
        order.setTotalMoney(totalMoney);
        order.setOrderDetails(orderDetails);
        orderRepository.save(order);

        // Xóa các mục đã chọn trong giỏ hàng sau khi đặt hàng thành công
        cart.getItems().removeIf(cartItem -> orderDTO.getSelectedItems().contains(cartItem.getId()));  // Xóa các mục đã chọn
        cartRepository.save(cart);

        // Trả về thông tin đơn hàng
        return OrderResponse.formOrder(order);
    }


    @Override
    public Page<OrderResponse> getAllOrderss(String orderCode, List<OrderStatus> status, LocalDateTime orderDate, Pageable pageable) {

        if (orderCode != null || status != null || orderDate != null) {
            // Lọc theo các tham số
            return orderRepository.filterOrders(orderCode, status, orderDate, pageable)
                    .map(OrderResponse::formOrder);  // Ánh xạ từ Order sang OrderResponse
        } else {
            // Nếu không có tham số lọc, lấy tất cả đơn hàng
//            Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
//                    Sort.by("createdAt").descending());
            return orderRepository.findAll(pageable)
                    .map(OrderResponse::formOrder);  // Ánh xạ từ Order sang OrderResponse
        }
    }


}


