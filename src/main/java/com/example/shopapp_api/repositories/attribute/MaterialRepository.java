package com.example.shopapp_api.repositories.attribute;

import com.example.shopapp_api.entities.attributes.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Integer> {
    boolean existsByName(String name);
}
