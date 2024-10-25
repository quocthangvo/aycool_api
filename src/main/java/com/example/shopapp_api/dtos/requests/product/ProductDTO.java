package com.example.shopapp_api.dtos.requests.product;

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
public class ProductDTO {
    @NotBlank(message = "Name is required")// bắt buộc nhập
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Sku is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String sku;

    private String description;

//    private String thumbnail;

    @NotNull(message = "Category is required")
    @JsonProperty("sub_category_id")
    private int subCategoryId;

    @NotNull(message = "Material is required")
    @JsonProperty("material_id")
    private int materialId;

//    private List<MultipartFile> files;
}
