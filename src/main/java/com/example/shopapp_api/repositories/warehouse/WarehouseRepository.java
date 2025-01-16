package com.example.shopapp_api.repositories.warehouse;

import com.example.shopapp_api.entities.products.Product;
import com.example.shopapp_api.entities.products.ProductDetail;
import com.example.shopapp_api.entities.warehouse.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
    Optional<Warehouse> findByProductDetailId(int productDetailId);


    @Query("SELECT w.product, SUM(w.quantity) AS totalQuantity, AVG(w.price) AS averagePrice " +
            "FROM Warehouse w " +
            "GROUP BY w.product")
    List<Object[]> findGroupedWarehouse();

    @Query("SELECT w.product, SUM(w.quantity) AS totalQuantity, AVG(w.price) AS averagePrice " +
            "FROM Warehouse w " +
            "WHERE w.product.name LIKE %:searchTerm% " +
            "GROUP BY w.product")
    List<Object[]> findGroupedWarehouseByName(@Param("searchTerm") String searchTerm);

    @Query("SELECT w.product, SUM(w.quantity) AS totalQuantity, AVG(w.price) AS averagePrice " +
            "FROM Warehouse w " +
            "WHERE (:subcategoryId IS NULL OR w.product.subCategory.id = :subcategoryId) " +
            "GROUP BY w.product")
    List<Object[]> findGroupedWarehouseBySubcategory(@Param("subcategoryId") Integer subcategoryId);

    // all sp của user

    @Query("SELECT w.product, SUM(w.quantity) AS totalQuantity, AVG(w.price) AS averagePrice " +
            "FROM Warehouse w " +
            "WHERE (:searchTerm IS NULL OR w.product.name LIKE %:searchTerm%) " +
            "AND (:subcategoryId IS NULL OR w.product.subCategory.id = :subcategoryId) " +
            "GROUP BY w.product")
    Page<Object[]> findGroupedWarehouseByNameAndSubcategory(Pageable pageable,
                                                            @Param("searchTerm") String searchTerm,
                                                            @Param("subcategoryId") Integer subcategoryId);


//    @Query("SELECT w.product, SUM(w.quantity) AS totalQuantity, AVG(w.price) AS averagePrice " +
//            "FROM Warehouse w " +
//            "WHERE (:subcategoryId IS NULL OR w.product.subCategory.id = :subcategoryId) " +
//            "AND LOWER(w.product.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
//            "GROUP BY w.product")
//    List<Object[]> findGroupedWarehouseBySubcategoryAndName(@Param("subcategoryId") Integer subcategoryId,
//                                                            @Param("searchTerm") String searchTerm);


//    @Query("SELECT w.product, SUM(w.quantity) AS totalQuantity, AVG(w.price) AS averagePrice " +
//            "FROM Warehouse w " +
//            "WHERE w.product.subCategory.id = :subCategoryId " +
//            "GROUP BY w.product")
//    List<Object[]> findGroupedWarehouseBySubCategory(@Param("subCategoryId") int subCategoryId);

    @Query("SELECT w.product, SUM(w.quantity) AS totalQuantity, AVG(w.price) AS averagePrice " +
            "FROM Warehouse w " +
            "WHERE w.product.subCategory.id = :subCategoryId " +
            "AND (:colorId IS NULL OR w.productDetail.color.id IN :colorId) " +
            "AND (:sizeIds IS NULL OR w.productDetail.size.id IN :sizeIds) " +
            "AND (:materialId IS NULL OR w.product.material.id = :materialId) " +
            "GROUP BY w.product")
    Page<Object[]> findGroupedWarehouseByFilters(
            @Param("subCategoryId") int subCategoryId,
            @Param("colorId") List<Integer> colorId,
            @Param("sizeIds") List<Integer> sizeIds,
            @Param("materialId") Integer materialId,
            Pageable pageable);


    // so luong bán tìm id của product detail
    List<Warehouse> findByProductDetail(ProductDetail productDetail);

    //search admin
//    @Query("SELECT w FROM Warehouse w WHERE LOWER(TRIM(w.productDetail.skuName)) " +
//            "LIKE LOWER(CONCAT('%', TRIM(:skuName), '%'))")
//    Page<Warehouse> findByProductDetailSkuName(@Param("skuName") String skuName, Pageable pageable);

    @Query("SELECT w FROM Warehouse w WHERE LOWER(TRIM(w.productDetail.skuName)) " +
            "LIKE LOWER(CONCAT('%', TRIM(:skuName), '%')) " +
            "AND (:subCategoryId IS NULL OR w.product.subCategory.id = :subCategoryId) " +
            "AND (:materialId IS NULL OR w.product.material.id = :materialId)")
    Page<Warehouse> findByProductDetailSkuNameAndSubCategoryIdAndMaterial(
            @Param("skuName") String skuName,
            @Param("subCategoryId") Integer subCategoryId,
            @Param("materialId") Integer materialId,
            Pageable pageable);


    // ds theo category
    @Query("SELECT w.product, SUM(w.quantity) AS totalQuantity, AVG(w.price) AS averagePrice " +
            "FROM Warehouse w " +
            "WHERE w.product.subCategory.category.id = :categoryId " +
            "GROUP BY w.product")
    Page<Object[]> findGroupedWarehouseByCategoryId(Pageable pageable, @Param("categoryId") int categoryId);


}
