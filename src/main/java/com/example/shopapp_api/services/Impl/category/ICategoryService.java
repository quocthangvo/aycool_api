package com.example.shopapp_api.services.Impl.category;

import com.example.shopapp_api.dtos.requests.category.CategoryDTO;
import com.example.shopapp_api.entities.categories.Category;
import com.example.shopapp_api.exceptions.DataNotFoundException;


import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO) throws DataNotFoundException;

    Category getCategoryById(int id);

    List<Category> getAllCategory();

    void deleteCategory(int id);

    Category updateCategory(int categoryId, CategoryDTO categoryDTO);
}
