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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
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

    @Override
    public OrderDetailResponse getTopSellingProduct() {
        // Truy vấn tất cả OrderDetails
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();

        // Tính tổng số lượng bán cho từng sản phẩm
        Map<Integer, Integer> productSales = new HashMap<>();
        for (OrderDetail orderDetail : orderDetails) {
            int productDetailId = orderDetail.getProductDetail().getId();
            productSales.put(productDetailId, productSales.getOrDefault(productDetailId, 0) + orderDetail.getQuantity());
        }

        // Sắp xếp các sản phẩm theo số lượng bán (giảm dần)
        List<Map.Entry<Integer, Integer>> sortedSales = new ArrayList<>(productSales.entrySet());
        sortedSales.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // Lấy ra sản phẩm bán chạy nhất
        if (sortedSales.isEmpty()) {
            return null; // Không có sản phẩm nào
        }

        // Lấy productDetailId của sản phẩm bán chạy nhất
        int topSellingProductDetailId = sortedSales.get(0).getKey();

        // Truy vấn OrderDetail của sản phẩm bán chạy nhất
        List<OrderDetail> topSellingOrderDetails = orderDetailRepository.findByProductDetailId(topSellingProductDetailId);

        // Trả về OrderDetailResponse
        if (!topSellingOrderDetails.isEmpty()) {
            // Lấy sản phẩm bán ít nhất từ danh sách OrderDetail
            return OrderDetailResponse.formOrderDetail(topSellingOrderDetails.get(0)); // Chỉ trả về 1 OrderDetail (nếu cần)
        } else {
            throw new RuntimeException("No OrderDetail found for productDetailId: " + topSellingOrderDetails);
        }
    }

    @Override
//    public List<OrderDetailResponse> getTopSellingProducts() {
//        // Truy vấn tất cả OrderDetails
//        List<OrderDetail> orderDetails = orderDetailRepository.findAll();
//
//        // Tính tổng số lượng bán cho từng sản phẩm (cộng dồn số lượng)
//        Map<Integer, Integer> productSales = new HashMap<>();
//        for (OrderDetail orderDetail : orderDetails) {
//            int productDetailId = orderDetail.getProductDetail().getId();
//            productSales.put(productDetailId, productSales.getOrDefault(productDetailId, 0) + orderDetail.getQuantity());
//        }
//
//        // Sắp xếp các sản phẩm theo số lượng bán (giảm dần)
//        List<Map.Entry<Integer, Integer>> sortedSales = new ArrayList<>(productSales.entrySet());
//        sortedSales.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
//
//        // Lưu trữ các sản phẩm bán chạy nhất
//        List<OrderDetailResponse> topSellingProducts = new ArrayList<>();
//        Set<Integer> processedProductIds = new HashSet<>(); // Lưu các productId đã xử lý
//
//        // Lấy ra tối đa 3 sản phẩm bán chạy nhất, mỗi sản phẩm phải có productId khác nhau
//        for (int i = 0; i < sortedSales.size(); i++) {
//            int productDetailId = sortedSales.get(i).getKey();
//            int productId = orderDetails.stream()
//                    .filter(od -> od.getProductDetail().getId() == productDetailId)
//                    .map(od -> od.getProductDetail().getProduct().getId())
//                    .findFirst()
//                    .orElseThrow(() -> new RuntimeException("Product ID not found for productDetailId: " + productDetailId));
//
//            // Kiểm tra xem sản phẩm này đã được thêm vào danh sách chưa
//            if (!processedProductIds.contains(productId)) {
//                // Lọc tất cả OrderDetails có cùng productId và chọn sản phẩm bán chạy nhất
//                List<OrderDetail> productOrderDetails = orderDetails.stream()
//                        .filter(od -> od.getProductDetail().getProduct().getId() == productId)
//                        .sorted((od1, od2) -> Integer.compare(od2.getQuantity(), od1.getQuantity()))  // Sắp xếp theo số lượng bán
//                        .collect(Collectors.toList());
//
//                // Lấy tất cả ProductDetails của cùng một sản phẩm
//                for (OrderDetail orderDetail : productOrderDetails) {
//                    int currentProductDetailId = orderDetail.getProductDetail().getId();
//
//                    // Chuyển đổi OrderDetail sang OrderDetailResponse
//                    OrderDetailResponse orderDetailResponse = OrderDetailResponse.formOrderDetail(orderDetail);
//
//                    // Thêm vào danh sách sản phẩm bán chạy nếu chưa có
//                    if (!processedProductIds.contains(productId)) {
//                        topSellingProducts.add(orderDetailResponse);
//                        processedProductIds.add(productId); // Đánh dấu là đã xử lý productId này
//                    }
//
//                    // Dừng lại khi đủ 3 sản phẩm bán chạy khác nhau
//                    if (topSellingProducts.size() >= 5) {
//                        return topSellingProducts;
//                    }
//                }
//            }
//        }
//
//        return topSellingProducts;
//    }
    public List<OrderDetailResponse> getTopSellingProducts() {
        // Truy vấn tất cả OrderDetails
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();

        // Tính tổng số lượng bán cho từng sản phẩm (cộng dồn số lượng)
        Map<Integer, Integer> productSales = new HashMap<>();
        for (OrderDetail orderDetail : orderDetails) {
            int productDetailId = orderDetail.getProductDetail().getId();
            productSales.put(productDetailId, productSales.getOrDefault(productDetailId, 0) + orderDetail.getQuantity());
        }

        // Sắp xếp các sản phẩm theo số lượng bán (giảm dần)
        List<Map.Entry<Integer, Integer>> sortedSales = new ArrayList<>(productSales.entrySet());
        sortedSales.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // Lưu trữ các sản phẩm bán chạy nhất
        List<OrderDetailResponse> topSellingProducts = new ArrayList<>();
        Set<Integer> processedProductIds = new HashSet<>(); // Lưu các productId đã xử lý

        // Lấy ra tối đa 5 sản phẩm bán chạy nhất, mỗi sản phẩm phải có productId khác nhau
        for (int i = 0; i < sortedSales.size(); i++) {
            int productDetailId = sortedSales.get(i).getKey();
            int productId = orderDetails.stream()
                    .filter(od -> od.getProductDetail().getId() == productDetailId)
                    .map(od -> od.getProductDetail().getProduct().getId())
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Product ID not found for productDetailId: " + productDetailId));

            // Kiểm tra xem sản phẩm này đã được thêm vào danh sách chưa
            if (!processedProductIds.contains(productId)) {
                // Lọc tất cả OrderDetails có cùng productId và chọn sản phẩm bán chạy nhất
                List<OrderDetail> productOrderDetails = orderDetails.stream()
                        .filter(od -> od.getProductDetail().getProduct().getId() == productId)
                        .sorted((od1, od2) -> Integer.compare(od2.getQuantity(), od1.getQuantity()))  // Sắp xếp theo số lượng bán
                        .collect(Collectors.toList());

                // Lấy tất cả ProductDetails của cùng một sản phẩm
                for (OrderDetail orderDetail : productOrderDetails) {
                    int currentProductDetailId = orderDetail.getProductDetail().getId();

                    // Chuyển đổi OrderDetail sang OrderDetailResponse
                    OrderDetailResponse orderDetailResponse = OrderDetailResponse.formOrderDetail(orderDetail);

                    // Thêm vào danh sách sản phẩm bán chạy nếu chưa có
                    if (!processedProductIds.contains(productId)) {
                        topSellingProducts.add(orderDetailResponse);
                        processedProductIds.add(productId); // Đánh dấu là đã xử lý productId này
                    }

                    // Dừng lại khi đủ 5 sản phẩm bán chạy khác nhau
                    if (topSellingProducts.size() >= 5) {
                        return topSellingProducts;
                    }
                }
            }
        }

        return topSellingProducts;
    }


    @Override
    public OrderDetailResponse getLowSellingProduct() {
        // Truy vấn tất cả OrderDetails
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();

        // Tính tổng số lượng bán cho từng sản phẩm
        Map<Integer, Integer> productSales = new HashMap<>();
        for (OrderDetail orderDetail : orderDetails) {
            int productDetailId = orderDetail.getProductDetail().getId();
            // Cộng dồn số lượng sản phẩm theo productDetailId
            productSales.put(productDetailId, productSales.getOrDefault(productDetailId, 0) + orderDetail.getQuantity());
        }

        // Sắp xếp các sản phẩm theo số lượng bán (tăng dần) để lấy sản phẩm bán ít nhất
        List<Map.Entry<Integer, Integer>> sortedSales = new ArrayList<>(productSales.entrySet());
        sortedSales.sort(Map.Entry.comparingByValue());  // Sắp xếp theo số lượng bán (tăng dần)

        // Lấy ra sản phẩm bán thấp nhất
        if (sortedSales.isEmpty()) {
            return null; // Không có sản phẩm nào
        }

        // Lấy productDetailId của sản phẩm bán thấp nhất
        int lowSellingProductDetailId = sortedSales.get(0).getKey();

        // Truy vấn OrderDetail của sản phẩm bán thấp nhất
        List<OrderDetail> lowSellingOrderDetails = orderDetailRepository.findByProductDetailId(lowSellingProductDetailId);

        // Trả về OrderDetailResponse cho sản phẩm bán thấp nhất
        if (!lowSellingOrderDetails.isEmpty()) {
            // Lấy sản phẩm bán ít nhất từ danh sách OrderDetail
            return OrderDetailResponse.formOrderDetail(lowSellingOrderDetails.get(0)); // Chỉ trả về 1 OrderDetail (nếu cần)
        } else {
            throw new RuntimeException("No OrderDetail found for productDetailId: " + lowSellingProductDetailId);
        }
    }


    //top sp bán chậm
    @Override
    public List<OrderDetailResponse> getLowSellingProducts() {
        // Truy vấn tất cả OrderDetails
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();

        // Tính tổng số lượng bán cho từng sản phẩm (cộng dồn số lượng)
        Map<Integer, Integer> productSales = new HashMap<>();
        for (OrderDetail orderDetail : orderDetails) {
            int productDetailId = orderDetail.getProductDetail().getId();
            productSales.put(productDetailId, productSales.getOrDefault(productDetailId, 0) + orderDetail.getQuantity());
        }

        // Sắp xếp các sản phẩm theo số lượng bán (giảm dần)
        List<Map.Entry<Integer, Integer>> sortedSales = new ArrayList<>(productSales.entrySet());
        sortedSales.sort(Map.Entry.comparingByValue());

        // Lưu trữ các sản phẩm bán chạy nhất
        List<OrderDetailResponse> topSellingProducts = new ArrayList<>();
        Set<Integer> processedProductIds = new HashSet<>(); // Lưu các productId đã xử lý

        // Lấy ra tối đa 3 sản phẩm bán chạy nhất, mỗi sản phẩm phải có productId khác nhau
        for (int i = 0; i < sortedSales.size(); i++) {
            int productDetailId = sortedSales.get(i).getKey();
            int productId = orderDetails.stream()
                    .filter(od -> od.getProductDetail().getId() == productDetailId)
                    .map(od -> od.getProductDetail().getProduct().getId())
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Product ID not found for productDetailId: " + productDetailId));

            // Kiểm tra xem sản phẩm này đã được thêm vào danh sách chưa
            if (!processedProductIds.contains(productId)) {
                // Lọc tất cả OrderDetails có cùng productId và chọn sản phẩm bán chạy nhất
                List<OrderDetail> productOrderDetails = orderDetails.stream()
                        .filter(od -> od.getProductDetail().getProduct().getId() == productId)
                        .sorted((od1, od2) -> Integer.compare(od2.getQuantity(), od1.getQuantity()))  // Sắp xếp theo số lượng bán
                        .collect(Collectors.toList());

                // Lấy tất cả ProductDetails của cùng một sản phẩm
                for (OrderDetail orderDetail : productOrderDetails) {
                    int currentProductDetailId = orderDetail.getProductDetail().getId();

                    // Chuyển đổi OrderDetail sang OrderDetailResponse
                    OrderDetailResponse orderDetailResponse = OrderDetailResponse.formOrderDetail(orderDetail);

                    // Thêm vào danh sách sản phẩm bán chạy nếu chưa có
                    if (!processedProductIds.contains(productId)) {
                        topSellingProducts.add(orderDetailResponse);
                        processedProductIds.add(productId); // Đánh dấu là đã xử lý productId này
                    }

                    // Dừng lại khi đủ 3 sản phẩm bán chạy khác nhau
                    if (topSellingProducts.size() >= 3) {
                        return topSellingProducts;
                    }
                }
            }
        }

        return topSellingProducts;
    }

}
