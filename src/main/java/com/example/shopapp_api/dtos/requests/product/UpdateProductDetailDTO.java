package com.example.shopapp_api.dtos.requests.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;

import lombok.*;


@Data
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor //khởi tạo mặc định k tham số
public class UpdateProductDetailDTO {

    @JsonProperty("sku_version")
    private String skuVersion;

    @Min(value = 1, message = "Số lượng phải lớn hơn 1")
    private int quantity;


    @JsonProperty("color_id")
    private int colorId;

    @JsonProperty("size_id")
    private int sizeId;
}
