package com.example.shopapp_api.controllers.attributes;

import com.example.shopapp_api.dtos.requests.attribute.ColorDTO;

import com.example.shopapp_api.dtos.responses.apiResponse.ApiResponse;
import com.example.shopapp_api.dtos.responses.apiResponse.MessageResponse;
import com.example.shopapp_api.entities.attributes.Color;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.services.Impl.attribute.IColorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/colors")
@RequiredArgsConstructor
public class ColorController {
    private final IColorService colorService;

    @PostMapping("")
    public ResponseEntity<?> createColor(
            @Valid @RequestBody ColorDTO colorDTO,
            BindingResult result) throws DataNotFoundException {
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            Color createColor = colorService.createColor(colorDTO);
            return ResponseEntity.ok(new ApiResponse<>("Thêm mới thành công", createColor));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<?>>> getAllColors() {
        try {
            List<Color> color = colorService.getAllColors();
            return ResponseEntity.ok(new ApiResponse<>("Thành công", color));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));

        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getColor(@PathVariable("id") int colorId) {
        try {
            Color color = colorService.getColorById(colorId);
            return ResponseEntity.ok(new ApiResponse<>("Thành công", color));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteColor(@PathVariable("id") int colorId) {
        try {
            colorService.deleteColor(colorId);
            return ResponseEntity.ok(new MessageResponse(String.format("Xóa màu sắc có id = %d thành công", colorId)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateColor(@PathVariable("id") int colorId,
                                         @Valid @RequestBody ColorDTO colorDTO) {
        try {
            Color updateColor = colorService.updateColor(colorId, colorDTO);
            return ResponseEntity.ok(new ApiResponse<>("Cập nhật thành công", updateColor));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }
}
