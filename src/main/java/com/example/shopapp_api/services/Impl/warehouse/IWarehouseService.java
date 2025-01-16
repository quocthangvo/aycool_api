package com.example.shopapp_api.services.Impl.warehouse;

import com.example.shopapp_api.dtos.responses.product.products.ProductResponse;
import com.example.shopapp_api.dtos.responses.warehouse.group.WarehouseGroupResponse;
import com.example.shopapp_api.dtos.responses.warehouse.WarehouseResponse;
import com.example.shopapp_api.entities.warehouse.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IWarehouseService {


    Warehouse getWarehouseById(int id);

    Page<WarehouseResponse> getAllWarehouse(Pageable pageable);

//    Page<WarehouseResponse> searchWarehousesBySkuName(String skuName, Pageable pageable);

    Page<WarehouseResponse> searchWarehousesBySkuNameAndSubCategory(String skuName, Integer subCategoryId, Integer materialId, Pageable pageable);
//    List<WarehouseGroupResponse> getGroupedWarehouse(int subCategoryId);

//    Page<WarehouseGroupResponse> getGroupedWarehouse(Pageable pageable);


//    Page<WarehouseGroupResponse> getGroupedWarehouse(Pageable pageable, String searchTerm);

    Page<WarehouseGroupResponse> getGroupedWarehouse(Pageable pageable, String searchTerm, Integer subCategoryId);

    Page<WarehouseGroupResponse> getGroupedWarehouseByFilter(
            int subCategoryId, List<Integer> colorIds, List<Integer> sizeIds,
            Integer materialIds, Pageable pageable);


    Page<WarehouseGroupResponse> getGroupedProductsByCategoryId(Pageable pageable, int categoryId);


}
