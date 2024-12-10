package com.example.shopapp_api.repositories.product;

import com.example.shopapp_api.entities.products.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByName(String name);

    boolean existsBySku(String sku);

    Page<Product> findAll(Pageable pageable);

    Optional<Product> findById(int productId);

    @Query("SELECT p FROM Product p WHERE " +
            "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:materialId IS NULL OR p.material.id = :materialId)")
    Page<Product> searchByNameAndMaterial(@Param("name") String name,
                                          @Param("materialId") Integer materialId,
                                          Pageable pageable);

    // Tìm sản phẩm theo categoryId
    Page<Product> findBySubCategoryCategoryId(int categoryId, Pageable pageable);


    // Phương thức lọc theo subCategoryId, màu sắc, kích thước và chất vải
    @Query("SELECT p FROM Product p " +
            "JOIN p.subCategory sc " +
            "JOIN p.productDetails pd " +
            "JOIN pd.color c " +
            "JOIN pd.size s " +
            "WHERE sc.id = :subCategoryId " +
            "AND (:colorId IS NULL OR c.id = :colorId) " +
            "AND (:sizeIds IS NULL OR s.id IN :sizeIds) " +
            "AND (:materialIds IS NULL OR p.material.id IN :materialIds) " +
            "ORDER BY p.createdAt DESC")
    Page<Product> findProductsByFilters(@Param("subCategoryId") int subCategoryId,
                                        @Param("colorId") Integer colorId,
                                        @Param("sizeIds") List<Integer> sizeIds,
                                        @Param("materialIds") List<Integer> materialIds,
                                        Pageable pageable);


    // Tìm sản phẩm theo subCategoryId
    Page<Product> findBySubCategoryId(int subCategoryId, Pageable pageable);

//    @Query("SELECT p FROM Product p WHERE p.subCategory.id = :subCategoryId")
//    Page<Product> findBySubCategoryId(@Param("subCategoryId") int subCategoryId, Pageable pageable);


    // Tìm sản phẩm theo tên
    Page<Product> findByNameContaining(String name, Pageable pageable);

    // Tìm sản phẩm theo tên và subCategoryId
    Page<Product> findByNameContainingAndSubCategoryId(String name, Integer subCategoryId, Pageable pageable);


}
