package com.example.shopapp_api.services.Serv.user;

import com.example.shopapp_api.dtos.responses.user.UserResponse;
import com.example.shopapp_api.entities.users.User;
import com.example.shopapp_api.repositories.user.UserRepository;
import com.example.shopapp_api.services.Impl.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor

public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserResponse getUserById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với id: " + id));
        return modelMapper.map(user, UserResponse.class);
    }

//    @Override
//    public UserResponse getUserById(int id) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với id: " + id));
//
//        // Chuyển đổi User sang UserResponse
//        return UserResponse.builder()
//                .id(user.getId())
//                .fullName(user.getFullName())
//                .email(user.getEmail())
//                .phoneNumber(user.getPhoneNumber())
//                .createdAt(user.getCreatedAt())
//                .updatedAt(user.getUpdatedAt())
//                .build();
//    }


    @Override
    public Page<UserResponse> getAllUsers(PageRequest pageRequest) {
        Page<User> userPage = userRepository.findAll(pageRequest);

        // Chuyển đổi Page<User> thành Page<UserResponse>
        return userPage.map(user -> modelMapper.map(user, UserResponse.class));

    }

    @Override
    public UserResponse lockUser(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với id: " + id));
        user.setActive(false);
        userRepository.save(user);

        return modelMapper.map(user, UserResponse.class);

    }

    @Override
    public UserResponse unlockUser(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với id: " + id));
        // Kiểm tra xem tài khoản có bị xóa mềm không
        if (user.isActive()) {
            throw new RuntimeException("Tài khoản đang hoạt động.");
        }
        user.setActive(true);
        userRepository.save(user);
        return modelMapper.map(user, UserResponse.class);

    }

    @Override
    public void deleteUser(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("không tìm thấy người dùng với id: " + id));
        if (user.isActive()) {
            throw new RuntimeException("Tài khoàn đang hoạt động, không thể xóa!!!");
        }
        userRepository.deleteById(id);
    }
}
