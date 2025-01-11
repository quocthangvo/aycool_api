package com.example.shopapp_api.dtos.requests.warehouse;

import com.example.shopapp_api.dtos.responses.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class PurchaseDTO {
    private Integer quantity;

    private Float price;

    @JsonProperty("product_detail_id")
    private Integer productDetailId;

    private LocalDateTime dateTime;

}
