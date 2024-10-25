package com.example.shopapp_api.services.Impl.attribute;

import com.example.shopapp_api.dtos.requests.attribute.SizeDTO;
import com.example.shopapp_api.entities.attributes.Size;
import com.example.shopapp_api.exceptions.DataNotFoundException;

import java.util.List;

public interface ISizeService {
    Size createSize(SizeDTO sizeDTO) throws DataNotFoundException;

    List<Size> getAllSizes();

    Size getSizeById(int id);

    void deleteSize(int id);

    Size updateSize(int sizeId, SizeDTO sizeDTO);
}
