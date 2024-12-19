package com.example.shopapp_api.dtos.requests.review;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class ReviewRequest {
    private int rating;  // Điểm đánh giá (1 đến 5)
    private String comment;
}
