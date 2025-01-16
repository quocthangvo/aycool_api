package com.example.shopapp_api.dtos.requests.order.revenue;

import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class RevenueData {
    private List<String> labels;
    private List<Float> datasets;
}
