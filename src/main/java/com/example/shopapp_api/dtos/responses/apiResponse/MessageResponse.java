package com.example.shopapp_api.dtos.responses.apiResponse;

import lombok.*;

@Data// dùng cho product
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class MessageResponse {
    private String message;
}
