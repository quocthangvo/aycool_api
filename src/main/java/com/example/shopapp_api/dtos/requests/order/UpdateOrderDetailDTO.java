package com.example.shopapp_api.dtos.requests.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class UpdateOrderDetailDTO {

    @Min(value = 0, message = "price must be > 0")
    private Float price;

    @Min(value = 1, message = "quantity must be >1")
    @JsonProperty("quantity")
    private int quantity;

    @Min(value = 0, message = "total money id must be > 0")
    @JsonProperty("total_money")
    private Float totalMoney;

}
