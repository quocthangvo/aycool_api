package com.example.shopapp_api.services.Serv.category;

import com.example.shopapp_api.dtos.requests.category.SubCategoryDTO;
import com.example.shopapp_api.dtos.responses.category.SubCategoryResponse;
import com.example.shopapp_api.entities.categories.Category;
import com.example.shopapp_api.entities.categories.SubCategory;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.repositories.category.CategoryRepository;
import com.example.shopapp_api.repositories.category.SubCategoryRepository;
import com.example.shopapp_api.services.Impl.category.ISubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubCategoryService implements ISubCategoryService {
    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public SubCategory createSubCategory(SubCategoryDTO subCategoryDTO) throws DataNotFoundException {

        Category category = categoryRepository.findById(
                        subCategoryDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy id danh mục"));

        SubCategory newSubCategory = SubCategory.builder()
                .name(subCategoryDTO.getName())
                .category(category)
                .build(); // tạo đối tượng rỗng rồi khởi tạo từng phần

//        newSubCategory.setCategory(category);

        return subCategoryRepository.save(newSubCategory);
    }

    @Override
    public SubCategoryResponse getSubCategoryById(int id) {
        SubCategory subCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục con với id: " + id));

        // Ánh xạ từ SubCategory sang SubCategoryResponse
        return SubCategoryResponse.formSubCategory(subCategory);
    }

    @Override
    public List<SubCategoryResponse> getAllSubCategory() {

//        return subCategoryRepository.findAll();
        // Lấy danh sách tất cả SubCategory
        List<SubCategory> subCategories = subCategoryRepository.findAll();

        // Chuyển đổi SubCategory thành SubCategoryResponse
        return subCategories.stream()
                .map(SubCategoryResponse::formSubCategory)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubCategory> getSubCategoryByCategoryId(int categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy id danh mục " + categoryId));
        return subCategoryRepository.findByCategoryId(categoryId);
    }

    @Override
    public void deleteSubCategory(int id) {
        getSubCategoryById(id);
        subCategoryRepository.deleteById(id);
    }

    @Override
    public SubCategory updateSubCategory(int subCategoryId, SubCategoryDTO subCategoryDTO) throws DataNotFoundException {
        SubCategory existingSubCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thâ id danh mục con" + subCategoryId));
        if (existingSubCategory != null) {
            existingSubCategory.setName(subCategoryDTO.getName());
            // Kiểm tra categoryId trong DTO, nếu có giá trị thì cập nhật danh mục cha
            if (subCategoryDTO.getCategoryId() != null) {
                // Tìm danh mục cha mới nếu categoryId không null
                Category existingCategory = categoryRepository.findById(subCategoryDTO.getCategoryId())
                        .orElseThrow(() -> new DataNotFoundException("Không tìm thấy danh mục với id: " +
                                subCategoryDTO.getCategoryId())); // Kiểm tra category id có tồn tại không

                existingSubCategory.setCategory(existingCategory); // Cập nhật danh mục nếu có
            }
//            existingSubCategory.setCategory(existingCategory);
            return subCategoryRepository.save(existingSubCategory);//trả về và lưu update db

        }
        return null;

    }
}
