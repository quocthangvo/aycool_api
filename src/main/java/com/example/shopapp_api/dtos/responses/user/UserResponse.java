package com.example.shopapp_api.dtos.responses.user;

import com.example.shopapp_api.entities.users.Role;
import com.example.shopapp_api.entities.users.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;


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

    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonProperty("date_of_birth")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;


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
