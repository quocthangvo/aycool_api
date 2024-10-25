package com.example.shopapp_api.services.Impl.attribute;

import com.example.shopapp_api.dtos.requests.attribute.ColorDTO;
import com.example.shopapp_api.entities.attributes.Color;
import com.example.shopapp_api.exceptions.DataNotFoundException;

import java.util.List;

public interface IColorService {
    Color createColor(ColorDTO colorDTO) throws DataNotFoundException;

    List<Color> getAllColors();

    Color getColorById(int id);

    void deleteColor(int id);

    Color updateColor(int colorId, ColorDTO colorDTO);
}
