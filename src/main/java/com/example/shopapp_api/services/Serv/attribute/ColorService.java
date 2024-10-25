package com.example.shopapp_api.services.Serv.attribute;

import com.example.shopapp_api.dtos.requests.attribute.ColorDTO;
import com.example.shopapp_api.entities.attributes.Color;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.repositories.attribute.ColorRepository;
import com.example.shopapp_api.services.Impl.attribute.IColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorService implements IColorService {
    private final ColorRepository colorRepository;

    @Override
    public Color createColor(ColorDTO colorDTO) throws DataNotFoundException {
        if (colorRepository.existsByName(colorDTO.getName())) {
            throw new DataNotFoundException(String.format("Màu sắc với tên '%s' đã tồn tại", colorDTO.getName()));
        }
        // Kiểm tra trùng tên
        if (colorRepository.existsByName(colorDTO.getName())) {
            throw new RuntimeException("Tên đã tồn tại, vui lòng nhập tên khác.");
        }
        Color createColor = Color.builder()
                .name(colorDTO.getName())
                .build(); // tạo đối tượng rỗng rồi khởi tạo từng phần
        return colorRepository.save(createColor);
    }

    @Override
    public List<Color> getAllColors() {
        return colorRepository.findAll();
    }

    @Override
    public Color getColorById(int id) {
        return colorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy màu sắc với id: " + id));
    }

    @Override
    public void deleteColor(int id) {
        getColorById(id);
        colorRepository.deleteById(id);
    }

    @Override
    public Color updateColor(int colorId, ColorDTO colorDTO) {

        Color existingColor = getColorById(colorId);
        // Kiểm tra xem tên có được thay đổi không
        if (!existingColor.getName().equals(colorDTO.getName())) {
            // Kiểm tra xem tên mới đã tồn tại chưa
            if (colorRepository.existsByName(colorDTO.getName())) {
                throw new RuntimeException("Tên màu đã tồn tại, vui lòng nhập tên khác.");
            }
            // Nếu tên thay đổi và không trùng, cập nhật tên mới
            existingColor.setName(colorDTO.getName());
        }
        return colorRepository.save(existingColor);
    }
}
