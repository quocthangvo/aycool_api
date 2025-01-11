package com.example.shopapp_api.dtos.requests.warehouse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class WarehouseDTO {
    @JsonProperty("product_id")
    private int productId;
//    private List<Integer> quantity;
//
//    private List<Float> price;
//
//    @JsonProperty("product_detail_id")
//    private List<Integer> productDetailId;


}
