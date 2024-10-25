package com.example.shopapp_api.repositories.category;

import com.example.shopapp_api.entities.categories.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByName(String name);
}
