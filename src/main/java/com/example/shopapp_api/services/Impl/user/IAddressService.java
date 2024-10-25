package com.example.shopapp_api.services.Impl.user;

import com.example.shopapp_api.dtos.requests.order.AddressDTO;
import com.example.shopapp_api.dtos.responses.order.AddressResponse;
import com.example.shopapp_api.exceptions.DataNotFoundException;

import java.util.List;

public interface IAddressService {
    AddressResponse createAddress(AddressDTO addressDTO) throws DataNotFoundException;

    AddressResponse getAddressById(int id);

    List<AddressResponse> getAllAddressByUserId(int userId) throws DataNotFoundException;

    void deleteAddress(int id);

    AddressResponse updateAddress(int addressId, AddressDTO addressDTO) throws DataNotFoundException;
}
