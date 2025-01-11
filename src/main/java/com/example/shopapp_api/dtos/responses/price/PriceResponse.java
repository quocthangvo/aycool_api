package com.example.shopapp_api.dtos.responses.price;

import com.example.shopapp_api.dtos.responses.BaseResponse;
import com.example.shopapp_api.dtos.responses.order.OrderDetailResponse;
import com.example.shopapp_api.dtos.responses.product.products.ProductImageResponse;
import com.example.shopapp_api.entities.orders.OrderDetail;
import com.example.shopapp_api.entities.prices.Price;
import com.example.shopapp_api.entities.products.ProductImage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceResponse extends BaseResponse {

    private int id;

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

    @JsonProperty("image_url")
    private List<ProductImageResponse> imageUrl;

    public static PriceResponse formPrice(Price price) {
        PriceResponse priceResponse = PriceResponse
                .builder()
                .id(price.getId())
                .sellingPrice(price.getSellingPrice())
                .promotionPrice(price.getPromotionPrice())
                .startDate(price.getStartDate())
                .endDate(price.getEndDate())
                .productDetailId(price.getProductDetail().getId())
                .productDetailName(price.getProductDetail().getProduct().getName())
                .color(price.getProductDetail().getColor().getName())
                .size(price.getProductDetail().getSize().getName())
                .imageUrl(mapToProductImageResponse(price.getProductDetail().getProduct().getProductImages()))  // Chuyển đổi danh sách ảnh thành ProductImageResponse
                .build();
        priceResponse.setCreatedAt(price.getCreatedAt());
        priceResponse.setUpdatedAt(price.getUpdatedAt());
        return priceResponse;
    }

    // Phương thức hỗ trợ chuyển đổi từ danh sách ProductImage sang ProductImageResponse
    private static List<ProductImageResponse> mapToProductImageResponse(List<ProductImage> productImages) {
        List<ProductImageResponse> productImageResponses = new ArrayList<>();
        for (ProductImage productImage : productImages) {
            ProductImageResponse imageResponse = new ProductImageResponse();
            imageResponse.setImageUrl(productImage.getImageUrl());  // Chuyển URL ảnh
            // Cập nhật thêm thông tin khác nếu cần từ ProductImage
            productImageResponses.add(imageResponse);
        }
        return productImageResponses;
    }


}
