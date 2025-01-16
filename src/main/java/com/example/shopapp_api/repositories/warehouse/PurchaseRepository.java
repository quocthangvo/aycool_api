package com.example.shopapp_api.repositories.warehouse;

import com.example.shopapp_api.entities.warehouse.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

    @Query("SELECT SUM(p.quantity) FROM Purchase p WHERE p.productDetail.id = :productDetailId")
    int findTotalQuantityByProductDetailId(@Param("productDetailId") int productDetailId);

    //    Page<Purchase> findByProductDetail_Product_NameContainingIgnoreCase(String productName, Pageable pageable);
    Page<Purchase> findByProductDetail_Product_NameContainingIgnoreCaseAndProductDetail_Product_SubCategory_Id(
            String productName, Integer subCategoryId, Pageable pageable);

    // Tìm chỉ theo tên sản phẩm
    Page<Purchase> findByProductDetail_Product_NameContainingIgnoreCase(String productName, Pageable pageable);

    // Tìm chỉ theo ID danh mục
    Page<Purchase> findByProductDetail_Product_SubCategory_Id(Integer subCategoryId, Pageable pageable);
}
