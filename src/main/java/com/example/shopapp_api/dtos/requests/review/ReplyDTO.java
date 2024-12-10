package com.example.shopapp_api.dtos.requests.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class ReplyDTO {
    @JsonProperty("review_id")
    private int reviewId; // Đánh giá cần trả lời

    private String content;

    @JsonProperty("user_id")
    private int userId;
}
