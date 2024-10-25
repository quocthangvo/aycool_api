package com.example.shopapp_api.services.Impl.attribute;

import com.example.shopapp_api.dtos.requests.attribute.MaterialDTO;
import com.example.shopapp_api.entities.attributes.Material;
import com.example.shopapp_api.exceptions.DataNotFoundException;

import java.util.List;

public interface IMaterialService {
    Material createMaterial(MaterialDTO materialDTO) throws DataNotFoundException;

    List<Material> getAllMaterials();

    Material getMaterialById(int id);

    void deleteMaterial(int id);

    Material updateMaterial(int materialId, MaterialDTO materialDTO);
}
