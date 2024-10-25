package com.example.shopapp_api.dtos.requests.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor //khởi tạo mặc định k tham số
public class SubCategoryDTO {
    @NotBlank(message = "Name is requires")// bắt buộc nhập
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @NotNull(message = "Category is required")
    @JsonProperty("category_id")
    private int categoryId;
}
