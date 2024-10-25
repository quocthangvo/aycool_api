package com.example.shopapp_api.controllers.categories;

import com.example.shopapp_api.dtos.requests.category.CategoryDTO;
import com.example.shopapp_api.dtos.responses.apiResponse.ApiResponse;
import com.example.shopapp_api.dtos.responses.apiResponse.MessageResponse;
import com.example.shopapp_api.entities.categories.Category;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.services.Impl.category.ICategoryService;
import com.example.shopapp_api.services.Serv.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;

    @PostMapping("")
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult result) throws DataNotFoundException {
        if (result.hasErrors()) {
            List<String> errorMessage = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        Category createCategory = categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok(new ApiResponse<>("Thêm mới thành công", createCategory));
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<Category>>> getALlCategories(
    ) {
        try {
            List<Category> categories = categoryService.getAllCategory();
            return ResponseEntity.ok(new ApiResponse<>("Thành công", categories));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable("id") int categoryId) {
        try {
            Category existingCategory = categoryService.getCategoryById(categoryId);
            return ResponseEntity.ok(new ApiResponse<>("Thành công", existingCategory));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));

        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteCategory(@PathVariable int id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(new MessageResponse(String.format("Xóa danh mục có id = %d thành công", id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable int id,
            @Valid @RequestBody CategoryDTO categoryDTO) {
        try {
            Category updateCategory = categoryService.updateCategory(id, categoryDTO);
            return ResponseEntity.ok(new ApiResponse<>("Cập nhật thành công", updateCategory));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }

    }
}
