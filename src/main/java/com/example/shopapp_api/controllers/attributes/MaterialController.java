package com.example.shopapp_api.controllers.attributes;

import com.example.shopapp_api.dtos.requests.attribute.MaterialDTO;
import com.example.shopapp_api.dtos.responses.apiResponse.ApiResponse;
import com.example.shopapp_api.dtos.responses.apiResponse.MessageResponse;
import com.example.shopapp_api.entities.attributes.Material;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.services.Impl.attribute.IMaterialService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/materials")
@RequiredArgsConstructor
public class MaterialController {
    private final IMaterialService materialService;

    @PostMapping("")
    public ResponseEntity<?> createMaterial(
            @Valid @RequestBody MaterialDTO materialDTO,
            BindingResult result) throws DataNotFoundException {
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            Material createMaterial = materialService.createMaterial(materialDTO);
            return ResponseEntity.ok(new ApiResponse<>("Thêm mới thành công", createMaterial));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<?>>> getAllMaterials() {
        try {
            List<Material> material = materialService.getAllMaterials();
            return ResponseEntity.ok(new ApiResponse<>("Thành công", material));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));

        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMaterial(@PathVariable("id") int materialId) {
        try {
            Material material = materialService.getMaterialById(materialId);
            return ResponseEntity.ok(new ApiResponse<>("Thành công", material));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMaterial(@PathVariable("id") int materialId) {
        try {
            materialService.deleteMaterial(materialId);
            return ResponseEntity.ok(new MessageResponse(String.format("Xóa chất liệu có id = %d thành công", materialId)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMaterial(@PathVariable("id") int materialId,
                                            @Valid @RequestBody MaterialDTO materialDTO) {
        try {
            Material updateMaterial = materialService.updateMaterial(materialId, materialDTO);
            return ResponseEntity.ok(new ApiResponse<>("Cập nhật thành công", updateMaterial));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }
}
