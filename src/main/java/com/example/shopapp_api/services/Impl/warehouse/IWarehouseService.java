package com.example.shopapp_api.services.Impl.warehouse;

import com.example.shopapp_api.dtos.responses.warehouse.group.WarehouseGroupResponse;
import com.example.shopapp_api.dtos.responses.warehouse.WarehouseResponse;
import com.example.shopapp_api.entities.warehouse.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IWarehouseService {


    Warehouse getWarehouseById(int id);

    Page<WarehouseResponse> getAllWarehouse(Pageable pageable);


    List<WarehouseGroupResponse> getGroupedWarehouse();
}
