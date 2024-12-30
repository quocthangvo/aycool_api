package com.example.shopapp_api.controllers.categories;

import com.example.shopapp_api.dtos.requests.category.SubCategoryDTO;
import com.example.shopapp_api.dtos.responses.apiResponse.ApiResponse;
import com.example.shopapp_api.dtos.responses.apiResponse.MessageResponse;
import com.example.shopapp_api.dtos.responses.category.SubCategoryResponse;
import com.example.shopapp_api.entities.categories.SubCategory;
import com.example.shopapp_api.services.Impl.category.ISubCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/sub_categories")
@RequiredArgsConstructor

public class SubCategoryController {
    private final ISubCategoryService subCategoryService;


    @PostMapping("")
    public ResponseEntity<?> createSubCategory(
            @Valid @RequestBody SubCategoryDTO subCategoryDTO,
            BindingResult result) {

        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            SubCategory createSubCategory = subCategoryService.createSubCategory(subCategoryDTO);
            return ResponseEntity.ok(new ApiResponse<>("Thêm mới thành công", createSubCategory));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<SubCategoryResponse>>> getALlSubCategories(
//            @RequestParam("page") int page,
//            @RequestParam("limit") int limit
    ) {

        try {
            List<SubCategoryResponse> subCategories = subCategoryService.getAllSubCategory();
            return ResponseEntity.ok(new ApiResponse<>("Thành công", subCategories));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable("id") int subCategoryId) {
        try {
            SubCategoryResponse existingSubCategory = subCategoryService.getSubCategoryById(subCategoryId);
            return ResponseEntity.ok(new ApiResponse<>("Thành công", existingSubCategory));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));

        }

    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<SubCategory>>> getSubCategoriesByCategoryId(@PathVariable int categoryId) {
        try {
            List<SubCategory> existingCategory = subCategoryService.getSubCategoryByCategoryId(categoryId);
            return ResponseEntity.ok(new ApiResponse<>("Thành công", existingCategory));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));

        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteSubCategory(@PathVariable int id) {
        try {
            subCategoryService.deleteSubCategory(id);
            return ResponseEntity.ok(new MessageResponse(String.format("Xóa danh mục con có id = %d thành công", id)));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSubCategory(
            @PathVariable int id,
            @Valid @RequestBody SubCategoryDTO subCategoryDTO) {
        try {
            SubCategory updateSubCategory = subCategoryService.updateSubCategory(id, subCategoryDTO);
            return ResponseEntity.ok(new ApiResponse<>("Cập nhật thành công", updateSubCategory));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }
}
