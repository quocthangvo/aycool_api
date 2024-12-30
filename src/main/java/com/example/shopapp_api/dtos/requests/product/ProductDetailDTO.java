package com.example.shopapp_api.dtos.requests.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor //khởi tạo mặc định k tham số
public class ProductDetailDTO {

    //    @NotBlank(message = "SKU version là bắt buộc nhập")// bắt buộc nhập
    @Size(min = 3, max = 100, message = "SKU version phải từ 3-100 kí tự")
    @JsonProperty("sku_version")
    private String skuVersion;

    @Min(value = 1, message = "Số lượng phải lớn hơn 1")
    private Integer quantity;

    @NotNull(message = "Danh mục sản phẩm là bắt buộc chọn")
    @JsonProperty("product_id")
    private int productId;

    @NotNull(message = "Màu sắc là bắt buộc chọn")
    @JsonProperty("color_id")
    private List<Integer> colorId;

    @NotNull(message = "Kích thước là bắt buộc chọn")
    @JsonProperty("size_id")
    private List<Integer> sizeId;

}
