package com.example.shopapp_api.dtos.requests.attribute;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class SizeDTO {
    @NotEmpty(message = "Màu sắc không được để trống")
    private String name;

    private String description;

}
