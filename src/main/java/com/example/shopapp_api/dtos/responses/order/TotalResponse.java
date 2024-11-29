package com.example.shopapp_api.dtos.responses.order;

import com.example.shopapp_api.entities.orders.Address;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class TotalResponse {
    @JsonProperty("details")
    private List<OrderDetailResponse> details; // Danh sách chi tiết sản phẩm

    @JsonProperty("total_money")
    private Float totalMoney; // Tổng tiền đơn hàng

    @JsonProperty("address_id")
    private AddressResponse addressResponse;

    @JsonProperty("status")
    private String statusResponse;

    public TotalResponse(List<OrderDetailResponse> details, Float totalMoney, AddressResponse addressResponse, StatusResponse statusResponse) {
        this.details = details;
        this.totalMoney = totalMoney;
        this.addressResponse = addressResponse;
        this.statusResponse = statusResponse.getStatus().getStatusDisplayName();
    }

}
