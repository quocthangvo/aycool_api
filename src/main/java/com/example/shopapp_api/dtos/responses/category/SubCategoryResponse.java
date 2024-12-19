package com.example.shopapp_api.dtos.responses.category;

import com.example.shopapp_api.dtos.responses.order.AddressResponse;
import com.example.shopapp_api.entities.categories.SubCategory;
import com.example.shopapp_api.entities.orders.Address;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubCategoryResponse {
    private int id;
    private String name;

    @JsonProperty("category_name")
    private String categoryName;

    public static SubCategoryResponse formSubCategory(SubCategory subCategory) {


        SubCategoryResponse subCategoryResponse = SubCategoryResponse.builder()
                .id(subCategory.getId())
                .name(subCategory.getName())
                .categoryName(subCategory.getCategory().getName())

                .build();

        return subCategoryResponse;
    }
}
