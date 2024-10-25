package com.example.shopapp_api.repositories.attribute;

import com.example.shopapp_api.entities.attributes.Color;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorRepository extends JpaRepository<Color, Integer> {
    boolean existsByName(String name);

}
