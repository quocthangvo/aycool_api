package com.example.shopapp_api.controllers.products;

import com.example.shopapp_api.dtos.requests.product.ProductDTO;
import com.example.shopapp_api.dtos.requests.product.ProductDetailDTO;
import com.example.shopapp_api.dtos.requests.product.UpdateProductDetailDTO;
import com.example.shopapp_api.dtos.responses.apiResponse.ApiResponse;
import com.example.shopapp_api.dtos.responses.apiResponse.MessageResponse;
import com.example.shopapp_api.dtos.responses.product.ProductListResponse;
import com.example.shopapp_api.dtos.responses.product.ProductResponse;
import com.example.shopapp_api.entities.products.Product;
import com.example.shopapp_api.entities.products.ProductDetail;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.services.Impl.product.IProductDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/product_details")
@RequiredArgsConstructor
public class ProductDetailController {
    private final IProductDetailService productDetailService;

    @PostMapping("")
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDetailDTO productDetailDTO,
            BindingResult result) {

        try {
            if (result.hasErrors()) { //kt tích hop data có lỗi k
                List<String> errorMessage = result.getFieldErrors()//trả về ds doi tuong FieldError
                        .stream()// chuyen FieldError thành stream cho phép xu ly chuoi thao tac
                        .map(FieldError::getDefaultMessage) //chuyển FieldError thành chuỗi thông báo lỗi (message).
                        .toList();//Stream thành danh sách các chuỗi thông báo lỗi
                return ResponseEntity.badRequest().body(errorMessage);
            }// xử lý validation

            List<ProductDetail> createProductDetail = productDetailService.createProductDetail(productDetailDTO);
            return ResponseEntity.ok(new ApiResponse<>("Thành công", createProductDetail));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));

        }
    }

    @GetMapping("/product/{product_id}")
    public ResponseEntity<ApiResponse<List<?>>> getALlProductDetails(@PathVariable("product_id") int productId
    ) {
        try {
            List<ProductDetail> productDetail = productDetailService.getAllProductDetailsByProductId(productId);
            return ResponseEntity.ok(new ApiResponse<>("Thành công", productDetail));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductDetailId(@PathVariable("id") int productDetailId) {
        try {
            ProductDetail existingProductDetail = productDetailService.getProductDetailById(productDetailId);
            return ResponseEntity.ok(new ApiResponse<>("Thành công", existingProductDetail));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteProductDetail(@PathVariable("id") int id) {
        try {
            productDetailService.deleteProductDetail(id);
            return ResponseEntity.ok(new MessageResponse(String.format("Xóa sản phẩm có id = %d thành công", id)));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProductDetail(
            @PathVariable int id,
            @Valid @RequestBody UpdateProductDetailDTO updateProductDetailDTO) {
        try {
            ProductDetail updateProductDetail = productDetailService.updateProductDetail(id, updateProductDetailDTO);
            return ResponseEntity.ok(new ApiResponse<>("Cập nhật thành công", updateProductDetail));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }
}
