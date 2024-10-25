package com.example.shopapp_api.repositories.category;

import com.example.shopapp_api.entities.categories.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Integer> {

    List<SubCategory> findByCategoryId(int categoryId);
}
