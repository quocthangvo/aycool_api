package com.example.shopapp_api.services.Impl.category;

import com.example.shopapp_api.dtos.requests.category.SubCategoryDTO;
import com.example.shopapp_api.dtos.responses.category.SubCategoryResponse;
import com.example.shopapp_api.entities.categories.SubCategory;
import com.example.shopapp_api.exceptions.DataNotFoundException;

import java.util.List;

public interface ISubCategoryService {
    SubCategory createSubCategory(SubCategoryDTO subCategoryDTO) throws DataNotFoundException;

    SubCategory getSubCategoryById(int id) throws DataNotFoundException;

//    List<SubCategory> getAllSubCategory();

    List<SubCategoryResponse> getAllSubCategory();
    
    List<SubCategory> getSubCategoryByCategoryId(int categoryId);

    void deleteSubCategory(int id);

    SubCategory updateSubCategory(int subCategoryId, SubCategoryDTO subCategoryDTO) throws DataNotFoundException;
}
