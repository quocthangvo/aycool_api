package com.example.shopapp_api.services.Impl.user;

import com.example.shopapp_api.dtos.requests.auth.UserRegisterDTO;
import com.example.shopapp_api.dtos.responses.user.UserResponse;
import com.example.shopapp_api.entities.users.User;

import java.util.Map;

public interface IAuthService {
    User register(UserRegisterDTO userRegisterDTO) throws Exception;

//    String login(String email, String password, int roleId) throws Exception;

    Map<String, Object> login(String email, String password) throws Exception;

}
