package com.example.shopapp_api.services.Impl.warehouse;

import com.example.shopapp_api.dtos.requests.warehouse.PurchaseDTO;
import com.example.shopapp_api.dtos.requests.warehouse.PurchaseItemDTO;
import com.example.shopapp_api.dtos.requests.warehouse.WarehouseDTO;
import com.example.shopapp_api.dtos.responses.warehouse.WarehouseResponse;
import com.example.shopapp_api.entities.warehouse.Purchase;
import com.example.shopapp_api.entities.warehouse.Warehouse;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPurchaseService {
    List<Purchase> createPurchases(List<PurchaseDTO> purchaseDTOList) throws DataNotFoundException;

    Purchase getPurchaseById(int id);

    Page<Purchase> getAllPurchase(Pageable pageable);

    void deletePurchase(int id);

    Purchase updatePurchase(int purchaseId, PurchaseItemDTO purchaseItemDTO);
}
