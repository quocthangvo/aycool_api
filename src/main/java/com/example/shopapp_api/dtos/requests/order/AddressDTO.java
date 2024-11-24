package com.example.shopapp_api.dtos.requests.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class AddressDTO {
    @Min(value = 1, message = "order id must be >0")
    @JsonProperty("user_id")
    private int userId;

    @NotBlank(message = "Full name is required")
    @JsonProperty("full_name")
    private String fullName;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, message = "Phone number must be least 10 characters")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotBlank(message = "Street name is required")
    @JsonProperty("street_name")
    private String streetName;

    @NotBlank(message = "City is required")
    private String city;

    private String district;

    private String ward;

}
