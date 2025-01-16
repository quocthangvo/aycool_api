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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    //get all sp trong data admin
    @Override
//    public Page<WarehouseResponse> searchWarehousesBySkuName(String skuName, Pageable pageable) {
//        return warehouseRepository.findByProductDetailSkuName(skuName, pageable)
//                .map(WarehouseResponse::formWarehouse);
//    }

    public Page<WarehouseResponse> searchWarehousesBySkuNameAndSubCategory(String skuName, Integer subCategoryId, Integer materialId, Pageable pageable) {
        return warehouseRepository.findByProductDetailSkuNameAndSubCategoryIdAndMaterial(skuName, subCategoryId, materialId, pageable)
                .map(WarehouseResponse::formWarehouse);
    }


    //all kho chung 1 id product
//    @Override
//    public Page<WarehouseGroupResponse> getGroupedWarehouse(Pageable pageable) {
//        List<Object[]> groupedData = warehouseRepository.findGroupedWarehouse();
//
//        List<WarehouseGroupResponse> responses = groupedData.stream().map(data -> {
//                    Product product = (Product) data[0]; // Thông tin sản phẩm
//                    Long totalQuantity = (Long) data[1]; // Tổng số lượng
//                    Double averagePrice = (Double) data[2]; // Giá trung bình
//
//                    // Tạo WarehouseGroupResponse
//                    WarehouseGroupResponse response = WarehouseGroupResponse.builder()
//                            .productId(ProductResponse.formProduct(product))
//                            .build();
//                    if (response == null || response.getProductId().getProductDetails().isEmpty() ||
//                            response.getProductId().getProductDetails().stream().allMatch(pd -> pd.getPrices().isEmpty())) {
//                        return null;
//                    }
//
//                    response.setCreatedAt(product.getCreatedAt());
//                    response.setUpdatedAt(product.getUpdatedAt());
//                    return response;
//                }).filter(Objects::nonNull)
//                .collect(Collectors.toList());
//
//        return new PageImpl<>(responses, pageable, responses.size());
//    }
//    @Override
//    public Page<WarehouseGroupResponse> getGroupedWarehouse(Pageable pageable, String searchTerm) {
//        List<Object[]> groupedData;
//
//        if (searchTerm != null && !searchTerm.isEmpty()) {
//            groupedData = warehouseRepository.findGroupedWarehouseByName(searchTerm);
//        } else {
//            groupedData = warehouseRepository.findGroupedWarehouse();
//        }
//
//        List<WarehouseGroupResponse> responses = groupedData.stream().map(data -> {
//                    Product product = (Product) data[0]; // Thông tin sản phẩm
//                    Long totalQuantity = (Long) data[1]; // Tổng số lượng
//                    Double averagePrice = (Double) data[2]; // Giá trung bình
//
//                    // Tạo WarehouseGroupResponse
//                    WarehouseGroupResponse response = WarehouseGroupResponse.builder()
//                            .productId(ProductResponse.formProduct(product))
//                            .build();
//                    if (response == null || response.getProductId().getProductDetails().isEmpty() ||
//                            response.getProductId().getProductDetails().stream().allMatch(pd -> pd.getPrices().isEmpty())) {
//                        return null;
//                    }
//
//                    response.setCreatedAt(product.getCreatedAt());
//                    response.setUpdatedAt(product.getUpdatedAt());
//                    return response;
//                }).filter(Objects::nonNull)
//                .collect(Collectors.toList());
//
//        return new PageImpl<>(responses, pageable, responses.size());
//    }

    // get all product có search user
    @Override
//    public Page<WarehouseGroupResponse> getGroupedWarehouse(Pageable pageable,
//                                                            String searchTerm, Integer subCategoryId) {
//        Page<Object[]> groupedData;
//
//
//        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
//            groupedData = warehouseRepository
//                    .findGroupedWarehouseByNameAndSubcategory(pageable, searchTerm.trim(), subCategoryId);
//        } else if (subCategoryId != null) {
//            groupedData = warehouseRepository
//                    .findGroupedWarehouseByNameAndSubcategory(pageable, null, subCategoryId);
//        } else {
//            groupedData = warehouseRepository
//                    .findGroupedWarehouseByNameAndSubcategory(pageable, null, null);
//        }
//
//        List<WarehouseGroupResponse> responses = groupedData.stream().map(data -> {
//                    Product product = (Product) data[0]; // Thông tin sản phẩm
//
//
//                    // Tạo WarehouseGroupResponse
//                    WarehouseGroupResponse response = WarehouseGroupResponse.builder()
//                            .productId(ProductResponse.formProduct(product))
//                            .build();
//                    //sp không có giá  ẩn sp di
//                    if (response == null || response.getProductId().getProductDetails().isEmpty() ||
//                            response.getProductId().getProductDetails().stream().allMatch(pd -> pd.getPrices().isEmpty())) {
//                        return null;
//                    }
//
//                    response.setCreatedAt(product.getCreatedAt());
//                    response.setUpdatedAt(product.getUpdatedAt());
//                    return response;
//                }).filter(Objects::nonNull)
//                .collect(Collectors.toList());
//
//        return new PageImpl<>(responses, pageable, responses.size());
//    }

    public Page<WarehouseGroupResponse> getGroupedWarehouse(Pageable pageable,
                                                            String searchTerm, Integer subCategoryId) {
        Page<Object[]> groupedData;

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            groupedData = warehouseRepository
                    .findGroupedWarehouseByNameAndSubcategory(pageable, searchTerm.trim(), subCategoryId);
        } else if (subCategoryId != null) {
            groupedData = warehouseRepository
                    .findGroupedWarehouseByNameAndSubcategory(pageable, null, subCategoryId);
        } else {
            groupedData = warehouseRepository
                    .findGroupedWarehouseByNameAndSubcategory(pageable, null, null);
        }

        List<WarehouseGroupResponse> responses = groupedData.stream().map(data -> {
                    Product product = (Product) data[0]; // Thông tin sản phẩm


                    // Tạo WarehouseGroupResponse
                    WarehouseGroupResponse response = WarehouseGroupResponse.builder()
                            .productId(ProductResponse.formProduct(product)) // Lấy productId từ ProductResponse

                            .build();

                    // Ẩn sản phẩm nếu không có giá
                    if (response == null || response.getProductId().getProductDetails().isEmpty() ||
                            response.getProductId().getProductDetails().stream().allMatch(pd -> pd.getPrices().isEmpty())) {
                        return null;
                    }

                    response.setCreatedAt(product.getCreatedAt());
                    response.setUpdatedAt(product.getUpdatedAt());

                    return response;
                }).filter(Objects::nonNull)
//                .sorted(Comparator.comparing(WarehouseGroupResponse::getCreatedAt).reversed())  // Sắp xếp theo createdAt mới nhất
                .collect(Collectors.toList());
        responses = responses.stream()
                .sorted(Comparator.comparing(WarehouseGroupResponse::getCreatedAt).reversed())  // Sắp xếp theo createdAt mới nhất
                .collect(Collectors.toList());


        return new PageImpl<>(responses, pageable, responses.size());
    }


    //all kho theo sub category
//    @Override
//    public List<WarehouseGroupResponse> getGroupedWarehouse(int subCategoryId) {
//
//        List<Object[]> groupedData = warehouseRepository.findGroupedWarehouseBySubCategory(subCategoryId);
//
//        return groupedData.stream().map(data -> {
//                    Product product = (Product) data[0]; // Thông tin sản phẩm
//                    Long totalQuantity = (Long) data[1]; // Tổng số lượng
//                    Double averagePrice = (Double) data[2]; // Giá trung bình
//
//                    // Tạo WarehouseGroupResponse
//                    WarehouseGroupResponse response = WarehouseGroupResponse.builder()
//                            .productId(ProductResponse.formProduct(product))
//                            .build();
//
//                    if (response == null || response.getProductId().getProductDetails().isEmpty() ||
//                            response.getProductId().getProductDetails().stream().allMatch(pd -> pd.getPrices().isEmpty())) {
//                        return null;
//                    }
//                    response.setCreatedAt(product.getCreatedAt());
//                    response.setUpdatedAt(product.getUpdatedAt());
//                    return response;
//
//                })
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
//    }

    @Override
    public Page<WarehouseGroupResponse> getGroupedWarehouseByFilter(
            int subCategoryId,
            List<Integer> colorIds,
            List<Integer> sizeIds,
            Integer materialId,
            Pageable pageable) {

        Page<Object[]> groupedData = warehouseRepository.findGroupedWarehouseByFilters(
                subCategoryId,
                colorIds == null || colorIds.isEmpty() ? null : colorIds,
                sizeIds == null || sizeIds.isEmpty() ? null : sizeIds,
                materialId,
                pageable);

        List<WarehouseGroupResponse> responses = groupedData.stream().map(data -> {
                    Product product = (Product) data[0]; // Thông tin sản phẩm

                    // Tạo WarehouseGroupResponse
                    WarehouseGroupResponse response = WarehouseGroupResponse.builder()
                            .productId(ProductResponse.formProduct(product))
                            .build();
                    if (response == null || response.getProductId().getProductDetails().isEmpty() ||
                            response.getProductId().getProductDetails().stream().allMatch(pd -> pd.getPrices().isEmpty())) {
                        return null;
                    }

                    response.setCreatedAt(product.getCreatedAt());
                    response.setUpdatedAt(product.getUpdatedAt());
                    return response;
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new PageImpl<>(responses, pageable, responses.size());
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


    ///get all sp theo category
    @Override
    public Page<WarehouseGroupResponse> getGroupedProductsByCategoryId(Pageable pageable, int categoryId) {

        Page<Object[]> groupedData = warehouseRepository.findGroupedWarehouseByCategoryId(pageable, categoryId);

        List<WarehouseGroupResponse> responses = groupedData.stream().map(data -> {
                    Product product = (Product) data[0]; // Thông tin sản phẩm

                    // Tạo WarehouseGroupResponse
                    WarehouseGroupResponse response = WarehouseGroupResponse.builder()
                            .productId(ProductResponse.formProduct(product))
                            .build();
                    if (response == null || response.getProductId().getProductDetails().isEmpty() ||
                            response.getProductId().getProductDetails().stream().allMatch(pd -> pd.getPrices().isEmpty())) {
                        return null;
                    }

                    response.setCreatedAt(product.getCreatedAt());
                    response.setUpdatedAt(product.getUpdatedAt());
                    return response;
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new PageImpl<>(responses, pageable, responses.size());
    }


}
