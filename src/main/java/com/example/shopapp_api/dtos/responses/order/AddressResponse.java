package com.example.shopapp_api.dtos.responses.order;

import com.example.shopapp_api.entities.orders.Address;
import com.example.shopapp_api.entities.orders.Order;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressResponse {
    private int id;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("street_name")
    private String streetName;

    @JsonProperty("city")
    private String city;

    private String district;

    private String ward;

    public static AddressResponse formAddress(Address address) {


        // Tạo đối tượng OrderResponse
        AddressResponse addressResponse = AddressResponse.builder()
                .id(address.getId())
                .userId(address.getUser().getId())
                .fullName(address.getFullName())
                .phoneNumber(address.getPhoneNumber())
                .streetName(address.getStreetName())
                .city(address.getCity())
                .district(address.getDistrict())
                .ward(address.getWard())
                .build();

        return addressResponse;
    }
}
