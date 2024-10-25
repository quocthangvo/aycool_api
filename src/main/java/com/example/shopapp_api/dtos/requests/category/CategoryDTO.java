package com.example.shopapp_api.dtos.requests.category;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor //khởi tạo mặc định k tham số
public class CategoryDTO {
    @NotEmpty(message = "Category name cannot be empty")
    private String name;
}
