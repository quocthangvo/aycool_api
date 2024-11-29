package com.example.shopapp_api.services.Impl.price;

import com.example.shopapp_api.dtos.requests.price.PriceDTO;
import com.example.shopapp_api.dtos.responses.price.PriceResponse;
import com.example.shopapp_api.entities.prices.Price;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IPriceService {
    Price createPrice(PriceDTO priceDTO) throws DataNotFoundException;

    Price getPriceById(int id) throws DataNotFoundException;

    List<Price> getAllPriceByProductDetailId(int productDetailId);

    PriceResponse getPriceByProductDetailId(int productDetailId);

    void deletePrice(int id) throws DataNotFoundException;

    PriceResponse updatePrice(int priceId, PriceDTO priceDTO) throws DataNotFoundException;

    Page<PriceResponse> getAllPrices(PageRequest pageRequest);

//    List<Price> createPrice(PriceDTO priceDTO) throws DataNotFoundException;
}
