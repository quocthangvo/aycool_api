package com.example.shopapp_api.controllers.orders;

import com.example.shopapp_api.dtos.requests.order.AddressDTO;
import com.example.shopapp_api.dtos.responses.apiResponse.ApiResponse;
import com.example.shopapp_api.dtos.responses.apiResponse.MessageResponse;
import com.example.shopapp_api.dtos.responses.order.AddressResponse;
import com.example.shopapp_api.services.Impl.user.IAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/addresses")
@RequiredArgsConstructor

public class AddressController {
    private final IAddressService addressService;

    @PostMapping("")
    public ResponseEntity<?> createAddress(
            @Valid @RequestBody AddressDTO addressDTO,
            BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            AddressResponse createAddress = addressService.createAddress(addressDTO);
            return ResponseEntity.ok(new ApiResponse<>("Thêm địa chỉ mới thành công", createAddress));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }

    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getAddressByUserId(
            @Valid @PathVariable("user_id") int userId
    ) {
        try {
            List<AddressResponse> addresses = addressService.getAllAddressByUserId(userId);
            return ResponseEntity.ok(new ApiResponse<>("Danh sách:", addresses));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAddressById(
            @Valid @PathVariable("id") int addressId
    ) {
        try {
            AddressResponse existingAddress = addressService.getAddressById(addressId);
            return ResponseEntity.ok(new ApiResponse<>("Lấy thành công:", existingAddress));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));

        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteAddress(@PathVariable int id) {
        try {
            addressService.deleteAddress(id);
            return ResponseEntity.ok(new MessageResponse(String.format("Xóa địa chỉ có id = %d thành công", id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    //admin làm
    public ResponseEntity<?> updateAddress(
            @PathVariable("id") int id,
            @Valid @RequestBody AddressDTO addressDTO) {

        try {
            AddressResponse updateAddress = addressService.updateAddress(id, addressDTO);
            return ResponseEntity.ok(new ApiResponse<>("Cập nhật thành công", updateAddress));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));

        }
    }
}
