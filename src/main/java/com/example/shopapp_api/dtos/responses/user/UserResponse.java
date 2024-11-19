package com.example.shopapp_api.dtos.responses.user;

import com.example.shopapp_api.dtos.responses.product.ProductResponse;
import com.example.shopapp_api.entities.products.Product;
import com.example.shopapp_api.entities.users.Role;
import com.example.shopapp_api.entities.users.Token;
import com.example.shopapp_api.entities.users.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private int id;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("email")
    private String email;

    private String active;

    private String avatar;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;


    private Role role;


    public static UserResponse formUser(User user) {
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .dateOfBirth(user.getDateOfBirth())
                .role(user.getRole())


                .build();

        return userResponse;
    }
}
