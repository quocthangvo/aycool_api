package com.example.shopapp_api.dtos.responses.product;

import com.example.shopapp_api.dtos.responses.BaseResponse;
import com.example.shopapp_api.entities.products.Product;
import com.example.shopapp_api.entities.products.ProductDetail;
import com.example.shopapp_api.entities.products.ProductImage;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor//khởi tạo all
@NoArgsConstructor//khởi tạo mặc định k tham số
@Builder//dùng để trả về build khi cần dùng đến create hay update sẽ gọi dc các phần tữ phái dưới

public class ProductResponse extends BaseResponse {

    private int id;

    private String name;

    private String sku;

    private String description;

    @JsonProperty("sub_category_id")
    private int subCategoryId;

    @JsonProperty("sub_category_name")
    private String subCategoryName;

    @JsonProperty("material_id")
    private int materialId;

    @JsonProperty("material_name")
    private String materialName;

    @JsonProperty("product_details")
    private List<ProductDetail> productDetails;

    @JsonProperty("product_images")
    private List<ProductImage> productImages;

//    @JsonProperty("color_id")
//    private int colorId;
//
//    @JsonProperty("size_id")
//    private int sizeId;
//
//
//    @JsonProperty("sku_version")
//    private String skuVersion;


    //nếu không mapping thì trả về theo kiểu thủ công tạo formProduct
    public static ProductResponse formProduct(Product product) {
        ProductResponse productResponse = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .sku(product.getSku())
                .description(product.getDescription())
                .subCategoryId(product.getSubCategory().getId())
                .subCategoryName(product.getSubCategory().getName())
                .materialId(product.getMaterial().getId())
                .materialName(product.getMaterial().getName())
                .productDetails(product.getProductDetails())
                .productImages(product.getProductImages())
                .build();
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }

}
