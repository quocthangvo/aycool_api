package com.example.shopapp_api.dtos.responses.product;

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

public class ProductSelectResponse {
    private int id;

    private String name;

    @JsonProperty("product_details")
    private List<ProductDetail> productDetails;

    @JsonProperty("product_images")
    private List<ProductImage> productImages;


    //nếu không mapping thì trả về theo kiểu thủ công tạo formProduct
    public static ProductSelectResponse formProduct(Product product) {
        ProductSelectResponse productSelectResponse = ProductSelectResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .productDetails(product.getProductDetails())
                .productImages(product.getProductImages())
                .build();

        return productSelectResponse;
    }
}
