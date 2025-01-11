package com.example.shopapp_api.repositories.warehouse;

import com.example.shopapp_api.entities.warehouse.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

    @Query("SELECT SUM(p.quantity) FROM Purchase p WHERE p.productDetail.id = :productDetailId")
    int findTotalQuantityByProductDetailId(@Param("productDetailId") int productDetailId);


}
