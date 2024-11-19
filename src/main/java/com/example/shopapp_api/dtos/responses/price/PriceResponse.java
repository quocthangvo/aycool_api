package com.example.shopapp_api.dtos.responses.price;

import com.example.shopapp_api.dtos.responses.order.OrderDetailResponse;
import com.example.shopapp_api.entities.orders.OrderDetail;
import com.example.shopapp_api.entities.prices.Price;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceResponse {
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

    @JsonProperty("product_detail_name")
    private String productDetailName;

    private String size;
    private String color;

    public static PriceResponse formPrice(Price price) {
        PriceResponse priceResponse = PriceResponse
                .builder()
                .sellingPrice(price.getSellingPrice())
                .promotionPrice(price.getPromotionPrice())
                .startDate(price.getStartDate())
                .endDate(price.getEndDate())
                .productDetailId(price.getProductDetail().getId())
                .productDetailName(price.getProductDetail().getProduct().getName())
                .color(price.getProductDetail().getColor().getName())
                .size(price.getProductDetail().getSize().getName())
                .build();
        return priceResponse;
    }
}
