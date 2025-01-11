package com.example.shopapp_api.dtos.requests.order;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class OrderStatsDTO {
    //    private LocalDate orderDate;
//    private Long totalOrders;
//    private Double totalRevenue;
    private Float totalMoney;
    private Long totalOrders;
}
