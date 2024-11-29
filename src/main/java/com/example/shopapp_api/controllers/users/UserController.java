package com.example.shopapp_api.controllers.users;

import com.example.shopapp_api.dtos.requests.auth.UserDTO;
import com.example.shopapp_api.dtos.requests.auth.UserUpdateDTO;
import com.example.shopapp_api.dtos.requests.order.AddressDTO;
import com.example.shopapp_api.dtos.responses.apiResponse.ApiResponse;
import com.example.shopapp_api.dtos.responses.apiResponse.MessageResponse;
import com.example.shopapp_api.dtos.responses.user.UserListResponse;
import com.example.shopapp_api.dtos.responses.user.UserResponse;
import com.example.shopapp_api.entities.users.User;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.services.Serv.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<?> getAllUser(@RequestParam("page") int page,
                                        @RequestParam("limit") int limit) {
        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<UserResponse> userPage = userService.getAllUsers(pageRequest);
        //tông số trang
        int totalPages = userPage.getTotalPages();//lấy ra tổng số trang
        List<UserResponse> userList = userPage.getContent();//từ productPage lấy ra ds các product getContent

        UserListResponse userListResponse = (UserListResponse
                .builder()
                .userResponseList(userList)
                .totalPages(totalPages)
                .build());
        return ResponseEntity.ok(new ApiResponse<>("Thành công", userListResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") int userId) {
        try {
            UserResponse existingUser = userService.getUserById(userId);
            return ResponseEntity.ok(new ApiResponse<>("Thành công", existingUser));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));

        }
    }

    @PutMapping("/lock/{id}")
    public ResponseEntity<?> lockUserById(@PathVariable("id") int userId) {
        try {
            UserResponse user = userService.lockUser(userId);
            return ResponseEntity.ok(new ApiResponse<>("Khóa tài khoàn thành công ", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }

    @PutMapping("/unlock/{id}")
    public ResponseEntity<?> unlockUserById(@PathVariable("id") int userId) {
        try {
            UserResponse user = userService.unlockUser(userId);
            return ResponseEntity.ok(new ApiResponse<>("Tài khoàn đã được mở khóa", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") int userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new MessageResponse("Tài khoản đã được xóa thành công"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String token) {
        try {
            String extractedToken = token.substring(7);
            User user = userService.getUserInfo(extractedToken);
            return ResponseEntity.ok(UserResponse.formUser(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));

        }
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(
            @PathVariable("userId") int userId,
            @Valid @RequestBody UserUpdateDTO userDTO
    ) throws DataNotFoundException {
        try {
            UserResponse updatedUser = userService.updateUser(userId, userDTO);
            return ResponseEntity.ok(new ApiResponse<>("Cập nhật thành công", updatedUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Lỗi: " + e.getMessage(), null));
        }

    }
}

