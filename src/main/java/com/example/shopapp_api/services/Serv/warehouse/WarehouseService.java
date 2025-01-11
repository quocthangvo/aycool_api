package com.example.shopapp_api.services.Serv.warehouse;

import com.example.shopapp_api.dtos.responses.product.products.ProductResponse;
import com.example.shopapp_api.dtos.responses.warehouse.group.WarehouseGroupResponse;
import com.example.shopapp_api.dtos.responses.warehouse.WarehouseResponse;
import com.example.shopapp_api.entities.cart.Cart;
import com.example.shopapp_api.entities.cart.CartItem;
import com.example.shopapp_api.entities.products.Product;
import com.example.shopapp_api.entities.products.ProductDetail;
import com.example.shopapp_api.entities.warehouse.Warehouse;
import com.example.shopapp_api.repositories.product.ProductDetailRepository;
import com.example.shopapp_api.repositories.warehouse.WarehouseRepository;
import com.example.shopapp_api.services.Impl.warehouse.IWarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class WarehouseService implements IWarehouseService {
    private final ProductDetailRepository productDetailRepository;
    private final WarehouseRepository warehouseRepository;


    @Override
    public Warehouse getWarehouseById(int id) {
        return null;
    }

    @Override
    public Page<WarehouseResponse> getAllWarehouse(Pageable pageable) {
        return warehouseRepository.findAll(pageable).map(WarehouseResponse::formWarehouse);
    }

    @Override
    public List<WarehouseGroupResponse> getGroupedWarehouse() {
        List<Object[]> groupedData = warehouseRepository.findGroupedWarehouse();

        return groupedData.stream().map(data -> {
            Product product = (Product) data[0]; // Thông tin sản phẩm
            Long totalQuantity = (Long) data[1]; // Tổng số lượng
            Double averagePrice = (Double) data[2]; // Giá trung bình

            // Tạo WarehouseGroupResponse
            WarehouseGroupResponse response = WarehouseGroupResponse.builder()
                    .productId(ProductResponse.formProduct(product))
                    .build();
            response.setCreatedAt(product.getCreatedAt());
            response.setUpdatedAt(product.getUpdatedAt());
            return response;
        }).collect(Collectors.toList());
    }

    // Hàm cập nhật số lượng trong kho
    public void updateWarehouseAfterOrder(Cart cart) {
        for (CartItem cartItem : cart.getItems()) {
            ProductDetail productDetail = cartItem.getProductDetail();
            List<Warehouse> warehouses = warehouseRepository.findByProductDetail(productDetail);
            for (Warehouse warehouse : warehouses) {
                int newSellQuantity = warehouse.getSellQuantity() + cartItem.getQuantity();  // Cộng số lượng bán vào
                warehouse.setSellQuantity(newSellQuantity);

                // Cập nhật remainingQuantity sau khi bán
                int newRemainingQuantity = warehouse.getQuantity() - newSellQuantity;
                warehouse.setRemainingQuantity(newRemainingQuantity);

                warehouseRepository.save(warehouse);  // Lưu thay đổi vào cơ sở dữ liệu
            }
        }
    }

}
