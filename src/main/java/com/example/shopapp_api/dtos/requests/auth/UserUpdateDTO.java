package com.example.shopapp_api.dtos.requests.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class UserUpdateDTO {
    @JsonProperty("full_name")
    private String fullName;


    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")

    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;
}
