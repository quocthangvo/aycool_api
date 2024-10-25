package com.example.shopapp_api.services.Serv.category;

import com.example.shopapp_api.dtos.requests.category.CategoryDTO;
import com.example.shopapp_api.entities.categories.Category;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.repositories.category.CategoryRepository;
import com.example.shopapp_api.services.Impl.category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(CategoryDTO categoryDTO) throws DataNotFoundException {
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new DataNotFoundException(String.format("Danh mục với tên '%s' đã tồn tại", categoryDTO.getName()));
        }
        Category newCategory = Category.builder()
                .name(categoryDTO.getName())
                .build(); // tạo đối tượng rỗng rồi khởi tạo từng phần
        return categoryRepository.save(newCategory);
    }

    @Override
    public Category getCategoryById(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với id: " + id));
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public void deleteCategory(int id) {
        getCategoryById(id);
        //xóa cứng
        categoryRepository.deleteById(id);
    }

    @Override
    public Category updateCategory(int categoryId, CategoryDTO categoryDTO) {
        Category exixstingCategory = getCategoryById(categoryId);
        exixstingCategory.setName(categoryDTO.getName());
        return categoryRepository.save(exixstingCategory);//trả về và lưu update db

    }
}
