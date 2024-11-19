package com.example.shopapp_api.dtos.requests.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor //khởi tạo mặc định k tham số
public class UserLoginDTO {
    @NotBlank(message = "Email is not null")//blank k dc để trống
    @Email(message = "INVALID_EMAIL_FORMAT")
    private String email;

    @NotBlank(message = "password is not null")
    private String password;

//    @Min(value = 1, message = "bạn cần role id")
//    @JsonProperty("role_id")
//    private Integer roleId;
}
