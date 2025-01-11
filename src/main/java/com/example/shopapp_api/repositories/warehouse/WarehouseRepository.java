package com.example.shopapp_api.repositories.warehouse;

import com.example.shopapp_api.dtos.requests.warehouse.WarehouseDTO;
import com.example.shopapp_api.entities.products.ProductDetail;
import com.example.shopapp_api.entities.warehouse.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
    Optional<Warehouse> findByProductDetailId(int productDetailId);


    @Query("SELECT w.product, SUM(w.quantity) AS totalQuantity, AVG(w.price) AS averagePrice " +
            "FROM Warehouse w " +
            "GROUP BY w.product")
    List<Object[]> findGroupedWarehouse();

    // so luong bán tìm id của product detail
    List<Warehouse> findByProductDetail(ProductDetail productDetail);

}
