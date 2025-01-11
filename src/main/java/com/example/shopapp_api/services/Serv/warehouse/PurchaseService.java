package com.example.shopapp_api.services.Serv.warehouse;

import com.example.shopapp_api.dtos.requests.warehouse.PurchaseDTO;
import com.example.shopapp_api.dtos.requests.warehouse.PurchaseItemDTO;
import com.example.shopapp_api.entities.products.ProductDetail;
import com.example.shopapp_api.entities.warehouse.Purchase;
import com.example.shopapp_api.entities.warehouse.Warehouse;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.repositories.product.ProductDetailRepository;
import com.example.shopapp_api.repositories.warehouse.PurchaseRepository;
import com.example.shopapp_api.repositories.warehouse.WarehouseRepository;
import com.example.shopapp_api.services.Impl.warehouse.IPurchaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseService implements IPurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final ProductDetailRepository productDetailRepository;
    private final WarehouseRepository warehouseRepository;

    @Override
    public List<Purchase> createPurchases(List<PurchaseDTO> purchaseDTOList) throws DataNotFoundException {
        List<Purchase> createdPurchases = new ArrayList<>();

        for (PurchaseDTO purchaseDTO : purchaseDTOList) {
            log.info("Processing purchase for product_detail_id: {}", purchaseDTO.getProductDetailId());

            // Lấy thông tin từ PurchaseDTO
            Integer productDetailId = purchaseDTO.getProductDetailId();
            Integer quantity = purchaseDTO.getQuantity();
            Float price = purchaseDTO.getPrice();  // Assuming sellingPrice corresponds to the price for purchase

            // Kiểm tra số lượng và giá hợp lệ
            if (quantity <= 0 || price <= 0) {
                throw new IllegalArgumentException("Quantity and price must be greater than 0.");
            }

            // Tìm ProductDetail theo ID
            ProductDetail productDetail = productDetailRepository.findById(productDetailId)
                    .orElseThrow(() -> new DataNotFoundException("ProductDetail with ID " + productDetailId + " not found"));

            // Tạo bản ghi Purchase
            Purchase purchase = new Purchase();
            purchase.setQuantity(quantity);
            purchase.setPrice(price);
            purchase.setProductDetail(productDetail);
            purchase.setDateTime(LocalDateTime.now());

            // Kiểm tra xem sản phẩm đã có trong kho chưa
            Warehouse warehouse = warehouseRepository.findByProductDetailId(productDetailId)
                    .orElse(null);

            if (warehouse == null) {
                // Nếu kho chưa có sản phẩm, tạo mới một bản ghi Warehouse
                warehouse = new Warehouse();
                warehouse.setProductDetail(productDetail);
                warehouse.setQuantity(quantity);  // Gán số lượng ban đầu
                warehouse.setPrice(price);  // Gán giá nhập mới nhất

                // Lấy productId từ ProductDetail
                warehouse.setProduct(productDetail.getProduct());

                // Tính remainingQuantity khi tạo mới
                warehouse.setRemainingQuantity(quantity - warehouse.getSellQuantity());

                // Lưu Warehouse mới vào DB
                warehouse = warehouseRepository.save(warehouse);
            } else {
                // Nếu sản phẩm đã có trong kho, cộng dồn số lượng và cập nhật giá nhập
                warehouse.setQuantity(warehouse.getQuantity() + quantity);
                warehouse.setPrice(price);  // Cập nhật giá nhập mới nhất

                // Tính remainingQuantity khi cập nhật số lượng
                warehouse.setRemainingQuantity((warehouse.getQuantity() + quantity) - warehouse.getSellQuantity());

                // Lấy productId từ ProductDetail
                warehouse.setProduct(productDetail.getProduct());


                warehouseRepository.save(warehouse);
            }

            // Lưu Purchase vào DB
            purchase.setWarehouse(warehouse);
            purchase = purchaseRepository.save(purchase);

            // Cập nhật số lượng trong ProductDetail
//            productDetail.setQuantity(productDetail.getQuantity() + quantity);
            productDetailRepository.save(productDetail);

            // Thêm vào danh sách các purchase đã tạo
            createdPurchases.add(purchase);
        }

        return createdPurchases;
    }


    @Override
    public Purchase getPurchaseById(int id) {
        return purchaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn nhập có id: " + id));
    }

    @Override
    public Page<Purchase> getAllPurchase(Pageable pageable) {
        return purchaseRepository.findAll(pageable);
    }

    @Override
    public void deletePurchase(int id) {
        // Tìm Purchase cần xóa
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn nhập có id: " + id));

        ProductDetail productDetail = purchase.getProductDetail();
        Warehouse warehouse = purchase.getWarehouse();

        // Xóa bản ghi Purchase
        purchaseRepository.delete(purchase);

        // Tính lại tổng số lượng trong Warehouse
        int totalQuantity = purchaseRepository.findTotalQuantityByProductDetailId(productDetail.getId());

        // Cập nhật lại số lượng tổng trong Warehouse
        warehouse.setQuantity(totalQuantity);

        // Lưu Warehouse đã cập nhật lại vào cơ sở dữ liệu
        warehouseRepository.save(warehouse);
    }


    @Override
    public Purchase updatePurchase(int purchaseId, PurchaseItemDTO purchaseItemDTO) {
        Purchase existingPurchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn nhập có id: " + purchaseId));

        // Cập nhật Purchase với thông tin mới
        existingPurchase.setQuantity(purchaseItemDTO.getQuantity());
        existingPurchase.setPrice(purchaseItemDTO.getPrice());

        // Lưu Purchase đã cập nhật vào cơ sở dữ liệu
        purchaseRepository.save(existingPurchase);

        // Tính lại tổng số lượng trong Warehouse cho ProductDetail liên quan
        ProductDetail productDetail = existingPurchase.getProductDetail();
        int totalQuantity = purchaseRepository.findTotalQuantityByProductDetailId(productDetail.getId());

        // Cập nhật số lượng tổng trong Warehouse
        Warehouse warehouse = existingPurchase.getWarehouse();
        warehouse.setQuantity(totalQuantity);

        // Lưu Warehouse đã cập nhật vào cơ sở dữ liệu
        warehouseRepository.save(warehouse);

        return existingPurchase;
    }
}
