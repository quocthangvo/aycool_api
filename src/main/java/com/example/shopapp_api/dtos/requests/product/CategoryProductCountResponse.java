package com.example.shopapp_api.dtos.requests.product;

import com.example.shopapp_api.dtos.responses.product.products.ProductSelectResponse;
import com.example.shopapp_api.entities.products.Product;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class CategoryProductCountResponse {
    private int categoryId;
    private String categoryName;
    private long totalProducts;

   
}
