package com.example.shopapp_api.services.Serv.attribute;

import com.example.shopapp_api.dtos.requests.attribute.SizeDTO;
import com.example.shopapp_api.entities.attributes.Color;
import com.example.shopapp_api.entities.attributes.Material;
import com.example.shopapp_api.entities.attributes.Size;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.repositories.attribute.SizeRepository;
import com.example.shopapp_api.services.Impl.attribute.ISizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SizeService implements ISizeService {
    private final SizeRepository sizeRepository;

    @Override
    public Size createSize(SizeDTO sizeDTO) throws DataNotFoundException {
        if (sizeRepository.existsByName(sizeDTO.getName())) {
            throw new DataNotFoundException(String.format("Chất liệu với tên '%s' đã tồn tại", sizeDTO.getName()));
        }
        Size createSize = Size.builder()
                .name(sizeDTO.getName())
                .description(sizeDTO.getDescription())
                .build(); // tạo đối tượng rỗng rồi khởi tạo từng phần
        return sizeRepository.save(createSize);
    }

    @Override
    public List<Size> getAllSizes() {
        return sizeRepository.findAll();
    }

    @Override
    public Size getSizeById(int id) {
        return sizeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kích thước với id: " + id));
    }

    @Override
    public void deleteSize(int id) {
        getSizeById(id);
        sizeRepository.deleteById(id);
    }

    @Override
    public Size updateSize(int sizeId, SizeDTO sizeDTO) {
        Size existingSize = getSizeById(sizeId);
        // Kiểm tra xem tên có được thay đổi không
        if (!existingSize.getName().equals(sizeDTO.getName())) {
            // Kiểm tra xem tên mới đã tồn tại chưa
            if (sizeRepository.existsByName(sizeDTO.getName())) {
                throw new RuntimeException("Tên màu đã tồn tại, vui lòng nhập tên khác.");
            }
            // Nếu tên thay đổi và không trùng, cập nhật tên mới
            existingSize.setName(sizeDTO.getName());
            existingSize.setDescription(sizeDTO.getDescription());
        }
        return sizeRepository.save(existingSize);
    }
}
