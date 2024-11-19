package com.example.shopapp_api.dtos.responses.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private String email;
    private String role;
    private String name;
}
