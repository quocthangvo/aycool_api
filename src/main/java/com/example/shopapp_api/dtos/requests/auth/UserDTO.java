package com.example.shopapp_api.dtos.requests.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

//anotation
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor //khởi tạo mặc định k tham số
public class UserDTO {
    //notBlank kt chuỗi k phải null, ko rỗng, và k chứa khoảng trắng
    //notNull kt giá trị k phải null(có thể dùng với bất kỳ dl nào)
    @NotBlank(message = "Name is not null")
    @JsonProperty("full_name")
    private String fullName;

    @NotBlank(message = "Email is not null")//blank k dc để trống
    private String email;

    @NotBlank(message = "password is not null")
    private String password;

    @JsonProperty("retype_password")
    private String retypePassword;

    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    @NotNull(message = "Role is required")
    @JsonProperty("role_id")
    private int role;


}
