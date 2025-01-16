package com.example.shopapp_api.dtos.requests.order.revenue;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class OrderRevenueDTO {
    private Object timePeriod;
    private Double totalRevenue;


}
