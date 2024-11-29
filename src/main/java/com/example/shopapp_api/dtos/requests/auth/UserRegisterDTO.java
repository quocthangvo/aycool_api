package com.example.shopapp_api.dtos.requests.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class UserRegisterDTO {
    @NotBlank(message = "Name is not null")
    @JsonProperty("full_name")
    private String fullName;

    @NotBlank(message = "Email is not null")//blank k dc để trống
    private String email;

    @NotBlank(message = "password is not null")
    private String password;

    @JsonProperty("retype_password")
    private String retypePassword;

    @NotNull(message = "Role is required")
    @JsonProperty("role_id")
    private int roleId;

}
