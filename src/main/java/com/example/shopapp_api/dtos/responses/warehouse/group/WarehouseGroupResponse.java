package com.example.shopapp_api.dtos.responses.warehouse.group;

import com.example.shopapp_api.dtos.responses.BaseResponse;
import com.example.shopapp_api.dtos.responses.product.products.ProductResponse;
import com.example.shopapp_api.entities.warehouse.Warehouse;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor//khởi tạo all
@NoArgsConstructor//khởi tạo mặc định k tham số
@Builder//dùng để trả về build khi cần dùng đến create hay update sẽ gọi dc các phần tữ phái dưới

public class WarehouseGroupResponse extends BaseResponse {
    private ProductResponse productId;

    public static WarehouseGroupResponse formWarehouseGroup(Warehouse warehouse) {
        WarehouseGroupResponse group = WarehouseGroupResponse.builder()
                .productId(ProductResponse.formProduct(warehouse.getProduct()))
                .build();
        group.setCreatedAt(warehouse.getCreatedAt());
        group.setUpdatedAt(warehouse.getUpdatedAt());
        return group;
    }
}
