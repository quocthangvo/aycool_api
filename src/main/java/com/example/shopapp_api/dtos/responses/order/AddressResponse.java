package com.example.shopapp_api.dtos.responses.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressResponse {
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
}
