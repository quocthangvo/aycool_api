package com.example.shopapp_api.controllers.users;

import com.example.shopapp_api.dtos.requests.auth.UserRegisterDTO;
import com.example.shopapp_api.dtos.requests.auth.UserLoginDTO;
import com.example.shopapp_api.dtos.responses.apiResponse.ApiResponse;
import com.example.shopapp_api.entities.users.User;
import com.example.shopapp_api.services.Impl.user.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/auths")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody UserRegisterDTO userRegisterDTO,
            BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            } //kiểm tra các ràng buộc (validation) trong dữ liệu đầu vào

//            if (!userRegisterDTO.getPassword().equals(userRegisterDTO.getRetypePassword())) {
//                return ResponseEntity.badRequest().body("Mật khẩu không trùng khớp");
//            }
            User user = authService.register(userRegisterDTO);
            return ResponseEntity.ok(new ApiResponse<>("Đăng ký tài khoản thành công", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        try {
            Map<String, Object> token = authService.login(userLoginDTO.getEmail(), userLoginDTO.getPassword()
//                    userLoginDTO.getRoleId() == null ? 2 : userLoginDTO.getRoleId()
            );
            return ResponseEntity.ok(new ApiResponse<>("Đăng nhập thành công", token));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));

        }
    }
}
