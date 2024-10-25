package com.example.shopapp_api.dtos.requests.price;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class PriceDTO {
    @JsonProperty("selling_price")
    private Float sellingPrice;

    @JsonProperty("promotion_price")
    private Float promotionPrice;

    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("end_date")
    private LocalDate endDate;

    @JsonProperty("product_detail_id")
    private int productDetailId;

}
