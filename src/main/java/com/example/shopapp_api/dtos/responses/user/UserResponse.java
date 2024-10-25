package com.example.shopapp_api.dtos.responses.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("email")
    private String email;

    private String active;

    private String avatar;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

}
