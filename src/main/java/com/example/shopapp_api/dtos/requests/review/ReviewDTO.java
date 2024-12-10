package com.example.shopapp_api.dtos.requests.review;


import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class ReviewDTO {

    @JsonProperty("comment")
    @Size(max = 200, message = "Comment must not exceed 200 characters")
    private String comment;

    @JsonProperty("rating")
    private Float rating;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("order_id")
    private int order;

    @JsonProperty("product_detail_id")
    private int productDetail;

//
//    @JsonProperty("product_id")
//    private int product;

    //    @JsonProperty("order_detail_id")
//    private int orderDetailId;


}
