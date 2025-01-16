package com.example.shopapp_api.services.Serv.order;

import com.example.shopapp_api.dtos.requests.order.OrderDTO;
import com.example.shopapp_api.dtos.requests.order.OrderStatusDTO;
import com.example.shopapp_api.dtos.responses.order.OrderResponse;
import com.example.shopapp_api.dtos.responses.order.StatusResponse;
import com.example.shopapp_api.entities.cart.Cart;
import com.example.shopapp_api.entities.cart.CartItem;
import com.example.shopapp_api.entities.coupon.Coupon;
import com.example.shopapp_api.entities.coupon.DiscountType;
import com.example.shopapp_api.entities.orders.*;
import com.example.shopapp_api.entities.orders.status.OrderStatus;
import com.example.shopapp_api.entities.orders.status.PaymentMethod;
import com.example.shopapp_api.entities.orders.status.PaymentStatus;
import com.example.shopapp_api.entities.prices.Price;
import com.example.shopapp_api.entities.products.ProductDetail;
import com.example.shopapp_api.entities.warehouse.Warehouse;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.repositories.cart.CartRepository;
import com.example.shopapp_api.repositories.coupon.CouponRepository;
import com.example.shopapp_api.repositories.coupon.CouponUsageRepository;
import com.example.shopapp_api.repositories.order.OrderDetailRepository;
import com.example.shopapp_api.repositories.price.PriceRepository;
import com.example.shopapp_api.repositories.product.ProductDetailRepository;
import com.example.shopapp_api.repositories.user.AddressRepository;
import com.example.shopapp_api.repositories.order.OrderRepository;
import com.example.shopapp_api.repositories.user.UserRepository;
import com.example.shopapp_api.repositories.warehouse.WarehouseRepository;
import com.example.shopapp_api.services.Impl.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
    private final CouponRepository couponRepository;
    private final CouponUsageRepository couponUsageRepository;
    private final WarehouseRepository warehouseRepository;

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
//    @PreAuthorize("hasRole('ADMIN')")
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

                    // Hủy số lượng đã bán và cập nhật remainingQuantity
                    for (OrderDetail orderDetail : existingOrder.getOrderDetails()) {
                        ProductDetail productDetail = orderDetail.getProductDetail();
                        List<Warehouse> warehouses = warehouseRepository.findByProductDetail(productDetail);

                        for (Warehouse warehouse : warehouses) {
                            int newSellQuantity = warehouse.getSellQuantity() - orderDetail.getQuantity();  // Trừ số lượng bán ra
                            warehouse.setSellQuantity(newSellQuantity);

                            // Cập nhật remainingQuantity sau khi hủy
                            int newRemainingQuantity = warehouse.getQuantity() + newSellQuantity;
                            warehouse.setRemainingQuantity(newRemainingQuantity);

                            warehouseRepository.save(warehouse);  // Lưu thay đổi vào cơ sở dữ liệu
                        }
                    }
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


                // Cập nhật số lượng bán (sellQuantity) và tính lại remainingQuantity
                List<Warehouse> warehouses = warehouseRepository.findByProductDetail(productDetail);
                for (Warehouse warehouse : warehouses) {
                    int newSellQuantity = warehouse.getSellQuantity() + cartItem.getQuantity();  // Cộng số lượng bán vào
                    warehouse.setSellQuantity(newSellQuantity);

                    // Cập nhật remainingQuantity sau khi bán
                    int newRemainingQuantity = warehouse.getQuantity() - warehouse.getSellQuantity();
                    warehouse.setRemainingQuantity(newRemainingQuantity);

                    warehouseRepository.save(warehouse);  // Lưu thay đổi vào cơ sở dữ liệu
                }
            }
        }

        // Lưu tổng tiền gốc
        order.setTotalMoney(totalMoney);


        // Kiểm tra mã giảm giá nếu có và áp dụng vào tổng tiền đơn hàng
        float totalMoneyAfterDiscount = totalMoney;
        if (orderDTO.getCouponId() != null) {
            Coupon coupon = couponRepository.findById(orderDTO.getCouponId())
                    .orElseThrow(() -> new DataNotFoundException("Mã giảm giá không hợp lệ"));

            if (!coupon.isStatus()) {
                throw new DataNotFoundException("Mã giảm giá không còn hiệu lực");
            }

            if (coupon.getEndDate() != null && coupon.getEndDate().isBefore(LocalDate.now())) {
                throw new DataNotFoundException("Mã giảm giá đã hết hạn");
            }

            if (coupon.getMinOrderValue() != null && totalMoney < coupon.getMinOrderValue()) {
                throw new DataNotFoundException("Giá trị đơn hàng chưa đủ để sử dụng mã giảm giá");
            }

            // Tính toán tổng tiền sau giảm giá

            if (coupon.getDiscountType() == DiscountType.PERCENT) {
                float discountAmount = totalMoney * (coupon.getDiscountValue() / 100);
                totalMoneyAfterDiscount -= discountAmount;
            } else if (coupon.getDiscountType() == DiscountType.FIXED_AMOUNT) {
                totalMoneyAfterDiscount -= coupon.getDiscountValue();
            }


            // Cập nhật tổng tiền cho đơn hàng
            order.setTotalMoneyAfterDiscount(totalMoneyAfterDiscount); // cập nhật tiền sau giảm giá

        } else {
            // Nếu không có mã giảm giá, không lưu totalMoneyAfterDiscount
            order.setTotalMoneyAfterDiscount(null); // Hoặc bỏ qua nếu không cần trường này
        }
//        order.setTotalMoney(totalMoney);
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

    @Transactional
    @Override
    // Phương thức áp dụng mã giảm giá vào đơn hàng
    public Order applyCouponToOrder(OrderDTO orderDTO) {
        // Lấy thông tin từ OrderDTO
        int orderId = Integer.parseInt(orderDTO.getOrderId()); // Lấy orderId từ OrderDTO
        int userId = orderDTO.getUserId(); // Lấy userId từ OrderDTO
        int couponId = orderDTO.getCouponId(); // Lấy couponId từ OrderDTO

        // Tìm đơn hàng bằng orderId
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Đơn hàng không tồn tại."));

        // Tìm coupon theo couponId
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("Mã giảm giá không hợp lệ hoặc đã hết hạn."));

        // Kiểm tra nếu tổng tiền đơn hàng đủ để áp dụng mã giảm giá
        if (order.getTotalMoney() < coupon.getMinOrderValue()) {
            throw new IllegalArgumentException("Tổng tiền của đơn hàng không đủ để sử dụng mã giảm giá.");
        }

        // Tính toán tổng tiền sau khi áp dụng giảm giá
        Float totalAfterDiscount = calculateTotalAfterDiscountWithUsage(userId, coupon, order.getTotalMoney());

        // Cập nhật đơn hàng với tổng tiền sau khi giảm giá
        order.setTotalMoneyAfterDiscount(totalAfterDiscount);
        order.setCoupon(coupon);  // Áp dụng coupon vào đơn hàng

        // Lưu đơn hàng vào cơ sở dữ liệu
        return orderRepository.save(order);
    }

    // Phương thức tính tổng tiền sau khi áp dụng mã giảm giá
    public Float calculateTotalAfterDiscountWithUsage(int userId, Coupon coupon, Float totalMoney) {
        // Kiểm tra điều kiện sử dụng mã giảm giá
        if (coupon == null || !coupon.isStatus()) {
            throw new IllegalArgumentException("Mã giảm giá không hợp lệ.");
        }

        // Kiểm tra nếu đơn hàng nhỏ hơn giá trị tối thiểu của mã giảm giá
        if (totalMoney < coupon.getMinOrderValue()) {
            throw new IllegalArgumentException("Giá trị đơn hàng không đủ để áp dụng mã giảm giá.");
        }

        // Tính toán giá trị giảm giá dựa trên loại mã giảm giá (tiền hay phần trăm)
        Float discountValue = 0.0f;
        if (coupon.getDiscountType() == DiscountType.FIXED_AMOUNT) {
            discountValue = coupon.getDiscountValue();
        } else if (coupon.getDiscountType() == DiscountType.PERCENT) {
            discountValue = totalMoney * (coupon.getDiscountValue() / 100);
        }

        // Đảm bảo tổng tiền không bị âm
        return Math.max(0, totalMoney - discountValue);
    }

    @Override
    public Map<String, Object> getFormattedTotalMoneyForAllOrders() {
        // Tính tổng doanh thu của các đơn hàng đã thanh toán
        Double totalMoney = 0.0;
        Double totalMoneyAfterDiscount = 0.0;

        //tổng doanh thu
//        Double totalMoney = orderRepository.calculateTotalMoneyForAllOrders();
//        // Tính tổng số đơn hàng
//        Long totalOrders = orderRepository.count();

        // Tính tổng doanh thu của các đơn hàng đã thanh toán
//        Double totalMoney = orderRepository.calculateTotalMoneyByPaymentStatus(PaymentStatus.PAID);

        // Tính tổng số đơn hàng đã thanh toán
        Long totalOrders = orderRepository.countByPaymentStatus(PaymentStatus.PAID);

        // Tính tổng doanh thu của các đơn hàng đã thanh toán
        List<Order> paidOrders = orderRepository.findAllByPaymentStatus(PaymentStatus.PAID);
        for (Order order : paidOrders) {
            if (order.getTotalMoneyAfterDiscount() != null) {
                totalMoneyAfterDiscount += order.getTotalMoneyAfterDiscount();  // Dùng nếu có totalMoneyAfterDiscount
            } else {
                totalMoney += order.getTotalMoney();  // Dùng nếu không có totalMoneyAfterDiscount
            }
        }

        // Cộng tổng
        totalMoney += totalMoneyAfterDiscount;


        // Định dạng tổng doanh thu dưới dạng chuỗi
//        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
//        String formattedTotalMoney = decimalFormat.format(totalMoney);
        Map<String, Object> response = new HashMap<>();
        response.put("totalMoney", totalMoney);
        response.put("totalOrders", totalOrders);

        return response;
    }

    // đơn hàng theo ngày
    @Override
    public Long getTotalOrdersToday() {
        return orderRepository.countTodayOrders();
    }

    //don hang thanh toan
    @Override
    public Double getTotalPaidOrders() {
        return orderRepository.calculateTotalPaidOrdersToday();
    }

    // lọc doanh thu

    @Override
    // Tính doanh thu theo thời gian tuần, tháng, quý, năm
    public Float getTotalRevenueByTimeRange(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.getTotalRevenueByTimeRange(status, startDate, endDate);
    }

    @Override
    public List<Float> getTotalRevenueByWeek(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate) {
        List<Float> weeklyRevenue = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            LocalDateTime weekStart = startDate.plusWeeks(i);
            LocalDateTime weekEnd = weekStart.plusWeeks(1).minusSeconds(1);
            Float revenue = orderRepository.getTotalRevenueByTimeRange(status, weekStart, weekEnd);
            weeklyRevenue.add(revenue != null ? revenue : 0.0f);
        }
        return weeklyRevenue;
    }

    @Override
    public List<Float> getTotalRevenueByMonth(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate) {
        List<Float> monthlyRevenue = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            LocalDateTime monthStart = startDate.plusMonths(i);
            LocalDateTime monthEnd = monthStart.plusMonths(1).minusSeconds(1);
            Float revenue = orderRepository.getTotalRevenueByTimeRange(status, monthStart, monthEnd);
            monthlyRevenue.add(revenue != null ? revenue : 0.0f);
        }
        return monthlyRevenue;
    }

    @Override
    public List<Float> getTotalRevenueByQuarter(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate) {
        List<Float> quarterlyRevenue = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            LocalDateTime quarterStart = startDate.plusMonths(i * 3);
            LocalDateTime quarterEnd = quarterStart.plusMonths(3).minusSeconds(1);
            Float revenue = orderRepository.getTotalRevenueByTimeRange(status, quarterStart, quarterEnd);
            quarterlyRevenue.add(revenue != null ? revenue : 0.0f);
        }
        return quarterlyRevenue;
    }

    @Override
    public List<Float> getTotalRevenueByYear(OrderStatus status) {
        LocalDateTime now = LocalDateTime.now();
        List<Float> yearlyRevenue = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            LocalDateTime yearStart = now.minusYears(i);  // Lấy năm hiện tại và năm từ 2024 đến 2025
            LocalDateTime yearEnd = yearStart.plusYears(1).minusSeconds(1);  // Đến cuối năm

            // Lấy dữ liệu doanh thu từ repository
            Float revenue = orderRepository.getTotalRevenueByTimeRange(status, yearStart, yearEnd);
            yearlyRevenue.add(revenue != null ? revenue : 0.0f);
        }
        return yearlyRevenue;
    }


}


