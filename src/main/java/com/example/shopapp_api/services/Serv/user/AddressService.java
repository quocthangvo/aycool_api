package com.example.shopapp_api.services.Serv.user;

import com.example.shopapp_api.dtos.requests.order.AddressDTO;
import com.example.shopapp_api.dtos.responses.order.AddressResponse;
import com.example.shopapp_api.entities.orders.Address;
import com.example.shopapp_api.entities.users.User;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.repositories.user.AddressRepository;
import com.example.shopapp_api.repositories.user.UserRepository;
import com.example.shopapp_api.services.Impl.user.IAddressService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService implements IAddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public AddressResponse createAddress(AddressDTO addressDTO) throws DataNotFoundException {
        User existingUser = userRepository
                .findById(addressDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Người dùng không tồn tại với id " + addressDTO.getUserId()));
//        Address createAddress = Address.builder()
//                .user(existingUser)
//                .fullName(addressDTO.getFullName())
//                .phoneNumber(addressDTO.getPhoneNumber())
//                .streetName(addressDTO.getStreetName())
//                .city(addressDTO.getCity())
//                .build(); // tạo đối tượng rỗng rồi khởi tạo từng phần
//        return addressRepository.save(createAddress);

        modelMapper.typeMap(AddressDTO.class, Address.class)
                .addMappings(mapper -> mapper.skip(Address::setId));
        Address createAddress = modelMapper.map(addressDTO, Address.class);
        createAddress.setUser(existingUser);
        // Lưu địa chỉ vào cơ sở dữ liệu
        Address savedAddress = addressRepository.save(createAddress);

        // Sử dụng ModelMapper để chuyển đổi sang AddressResponse
        return modelMapper.map(savedAddress, AddressResponse.class);

    }

    @Override
    public AddressResponse getAddressById(int id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ với id: " + id));
        return modelMapper.map(address, AddressResponse.class);
    }

    @Override
    public List<AddressResponse> getAllAddressByUserId(int userId) throws DataNotFoundException {
//        // Lấy người dùng hiện tại
//        User existingUser = userRepository.findById(userId)
//                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy user id: " + userId));
//
//        // Kiểm tra vai trò của người dùng
//        if (!existingUser.getRole().getName().equals(Role.USER)) {
//            throw new SecurityException("Người dùng không có quyền truy cập vào địa chỉ này");
//        }

        // Lấy danh sách địa chỉ cho người dùng
        List<Address> addresses = addressRepository.findByUserId(userId);
        if (addresses.isEmpty()) {
            throw new RuntimeException("Không có địa chỉ nào cho người dùng với ID " + userId);
        }

        // Sử dụng ModelMapper để ánh xạ từ Address sang AddressResponse
        return addresses.stream()
                .map(address -> modelMapper.map(address, AddressResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAddress(int id) {
        getAddressById(id);
        //xóa cứng
        addressRepository.deleteById(id);
    }

    @Override
    public AddressResponse updateAddress(int addressId, AddressDTO addressDTO) throws DataNotFoundException {
        // Lấy địa chỉ hiện có dưới dạng Address
        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy địa chỉ với id: " + addressId));

        User existingUser = userRepository.findById(addressDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy user id: " + addressDTO.getUserId()));

        // Kiểm tra xem địa chỉ này có thuộc về người dùng không
        if (existingAddress.getUser().getId() != existingUser.getId()) {
            throw new SecurityException("Địa chỉ này không thuộc về người dùng hiện tại.");
        }
        existingAddress.setFullName(addressDTO.getFullName());
        existingAddress.setPhoneNumber(addressDTO.getPhoneNumber());
        existingAddress.setStreetName(addressDTO.getStreetName());
        existingAddress.setCity(addressDTO.getCity());
        // Lưu địa chỉ đã cập nhật
        Address updatedAddress = addressRepository.save(existingAddress);

        // Sử dụng ModelMapper để ánh xạ từ Address sang AddressResponse
        return modelMapper.map(updatedAddress, AddressResponse.class);

    }
}
