package com.example.shopapp_api.dtos.responses.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewListResponse {
    private List<ReviewResponse> reviewResponseList; // truyền list
    private int totalPages;
    private long totalRecords;
    private long totalStars;

}
