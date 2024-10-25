package com.example.shopapp_api.services.Serv.attribute;

import com.example.shopapp_api.dtos.requests.attribute.MaterialDTO;
import com.example.shopapp_api.entities.attributes.Material;
import com.example.shopapp_api.entities.attributes.Size;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.repositories.attribute.MaterialRepository;
import com.example.shopapp_api.services.Impl.attribute.IMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialService implements IMaterialService {
    private final MaterialRepository materialRepository;

    @Override
    public Material createMaterial(MaterialDTO materialDTO) throws DataNotFoundException {
        if (materialRepository.existsByName(materialDTO.getName())) {
            throw new DataNotFoundException(String.format("Chất liệu với tên '%s' đã tồn tại", materialDTO.getName()));
        }
        Material createMaterial = Material.builder()
                .name(materialDTO.getName())
                .build(); // tạo đối tượng rỗng rồi khởi tạo từng phần
        return materialRepository.save(createMaterial);
    }

    @Override
    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    @Override
    public Material getMaterialById(int id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chất liệu với id: " + id));
    }

    @Override
    public void deleteMaterial(int id) {
        getMaterialById(id);
        materialRepository.deleteById(id);
    }

    @Override
    public Material updateMaterial(int materialId, MaterialDTO materialDTO) {
        Material existingMaterial = getMaterialById(materialId);
        // Kiểm tra xem tên có được thay đổi không
        if (!existingMaterial.getName().equals(materialDTO.getName())) {
            // Kiểm tra xem tên mới đã tồn tại chưa
            if (materialRepository.existsByName(materialDTO.getName())) {
                throw new RuntimeException("Tên màu đã tồn tại, vui lòng nhập tên khác.");
            }
            // Nếu tên thay đổi và không trùng, cập nhật tên mới
            existingMaterial.setName(materialDTO.getName());
        }
        return materialRepository.save(existingMaterial);
    }
}
