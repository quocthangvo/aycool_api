package com.example.shopapp_api.dtos.responses.product;

import com.example.shopapp_api.dtos.responses.BaseResponse;
import com.example.shopapp_api.entities.products.Product;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor//khởi tạo all
@NoArgsConstructor//khởi tạo mặc định k tham số
@Builder//dùng để trả về build khi cần dùng đến create hay update sẽ gọi dc các phần tữ phái dưới

public class ProductResponse extends BaseResponse {

    private String name;

    private String sku;

    private String description;

    @JsonProperty("sub_category_id")
    private int subCategoryId;

    private String subCategoryName;

    @JsonProperty("material_id")
    private int materialId;

    private String materialName;

    //nếu không mapping thì trả về theo kiểu thủ công tạo formProduct
    public static ProductResponse formProduct(Product product) {
        ProductResponse productResponse = ProductResponse.builder()
                .name(product.getName())
                .sku(product.getSku())
                .description(product.getDescription())
                .subCategoryId(product.getSubCategory().getId())
                .subCategoryName(product.getSubCategory().getName())
                .materialId(product.getMaterial().getId())
                .materialName(product.getMaterial().getName())
                .build();
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }

}
