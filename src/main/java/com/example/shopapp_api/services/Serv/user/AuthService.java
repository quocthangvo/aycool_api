package com.example.shopapp_api.services.Serv.user;

import com.example.shopapp_api.components.JwtTokenUtil;
import com.example.shopapp_api.dtos.requests.auth.UserRegisterDTO;
import com.example.shopapp_api.entities.users.Role;
import com.example.shopapp_api.entities.users.User;
import com.example.shopapp_api.exceptions.DataNotFoundException;
import com.example.shopapp_api.exceptions.PermissionDenyException;
import com.example.shopapp_api.repositories.user.RoleRepository;
import com.example.shopapp_api.repositories.user.UserRepository;
import com.example.shopapp_api.services.Impl.user.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;


    @Override
    public User register(UserRegisterDTO userRegisterDTO) throws Exception {
        String email = userRegisterDTO.getEmail();
        //kiểm tra tồn tại
        if (userRepository.existsByEmail(email)) {
            throw new DataIntegrityViolationException("Email đã tồn tại");
        }
        //kt mật khẩu trùng
        String password = userRegisterDTO.getPassword();
        String retypePassword = userRegisterDTO.getRetypePassword();
        if (!password.equals(retypePassword)) {
            throw new DataNotFoundException("Mật khẩu không trùng khớp.");
        }
        //kt Role
        Role role = roleRepository.findById(
                userRegisterDTO.getRoleId()).orElseThrow(() -> new DataNotFoundException("Không tìm thấy quyền"));
        if (role.getName().toUpperCase().equals(Role.ADMIN)) {
            throw new PermissionDenyException("Bạn không thể đăng kí 1 tài khoản admin ");
        }
        // Mã hóa mật khẩu nếu mật khẩu trước khi lưu
        String encodedPassword = passwordEncoder.encode(password);


        //convert từ userRegisterDTO => user
        User newUser = User.builder()
                .fullName(userRegisterDTO.getFullName())
                .email(userRegisterDTO.getEmail())
                .password(encodedPassword)
                .active(true)
                .build();
        newUser.setRole(role);


        return userRepository.save(newUser);
    }

    @Override
    public String login(String email, String password) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new DataNotFoundException("Email hoặc mật khẩu không hợp lệ");
        }
        //return optionalUser.get(); //muốn trả về JWT token
        User existingUser = optionalUser.get(); // tìm thấy lấy ra đối tượng user

        // Kiểm tra xem tài khoản có bị khóa không
        if (!existingUser.isActive()) {
            throw new RuntimeException("Tài khoản đã bị khóa. Vui lòng liên hệ với quản trị viên.");
        }
        //check pass
        if (existingUser.getPassword() != null && !existingUser.getPassword().isEmpty()) {
            // Kiểm tra xem mật khẩu đã mã hóa có khớp với mật khẩu nhập vào không
            if (!passwordEncoder.matches(password, existingUser.getPassword())) {
                throw new BadCredentialsException("Email hoặc mật khẩu không chính xác");
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                email, password,
                existingUser.getAuthorities()
        );
        //authenticate with Java Spring boot
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(optionalUser.get());

    }
}
