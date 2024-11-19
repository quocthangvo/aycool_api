package com.example.shopapp_api.services.Impl.user;

import com.example.shopapp_api.dtos.responses.user.UserResponse;
import com.example.shopapp_api.entities.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IUserService {
    UserResponse getUserById(int id);

    Page<UserResponse> getAllUsers(PageRequest pageRequest);

    UserResponse lockUser(int id);

    UserResponse unlockUser(int id);

    void deleteUser(int id);

    User getUserInfo(String token) throws Exception;
}
