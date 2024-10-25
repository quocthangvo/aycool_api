package com.example.shopapp_api.controllers.attributes;


import com.example.shopapp_api.dtos.requests.attribute.SizeDTO;
import com.example.shopapp_api.dtos.responses.apiResponse.ApiResponse;
import com.example.shopapp_api.dtos.responses.apiResponse.MessageResponse;
import com.example.shopapp_api.entities.attributes.Size;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.services.Impl.attribute.ISizeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/sizes")
@RequiredArgsConstructor
public class SizeController {
    private final ISizeService sizeService;

    @PostMapping("")
    public ResponseEntity<?> createSize(
            @Valid @RequestBody SizeDTO sizeDTO,
            BindingResult result) throws DataNotFoundException {
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            Size createSize = sizeService.createSize(sizeDTO);
            return ResponseEntity.ok(new ApiResponse<>("Thêm mới thành công", createSize));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<?>>> getAllSizes() {
        try {
            List<Size> sizes = sizeService.getAllSizes();
            return ResponseEntity.ok(new ApiResponse<>("Thành công", sizes));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));

        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSize(@PathVariable("id") int sizeId) {
        try {
            Size size = sizeService.getSizeById(sizeId);
            return ResponseEntity.ok(new ApiResponse<>("Thành công", size));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSize(@PathVariable("id") int sizeId) {
        try {
            sizeService.deleteSize(sizeId);
            return ResponseEntity.ok(new MessageResponse(String.format("Xóa màu sắc có id = %d thành công", sizeId)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSize(@PathVariable("id") int sizeId,
                                        @Valid @RequestBody SizeDTO sizeDTO) {
        try {
            Size updateSize = sizeService.updateSize(sizeId, sizeDTO);
            return ResponseEntity.ok(new ApiResponse<>("Cập nhật thành công", updateSize));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }
}
