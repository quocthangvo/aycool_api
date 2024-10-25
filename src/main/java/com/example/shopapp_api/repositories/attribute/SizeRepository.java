package com.example.shopapp_api.repositories.attribute;

import com.example.shopapp_api.entities.attributes.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SizeRepository extends JpaRepository<Size, Integer> {
    boolean existsByName(String name);
}
