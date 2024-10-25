package com.example.shopapp_api.controllers.users;

import com.example.shopapp_api.dtos.responses.apiResponse.ApiResponse;
import com.example.shopapp_api.entities.users.Role;
import com.example.shopapp_api.services.Serv.user.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping("")
    public ResponseEntity<?> getAllRole() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(new ApiResponse<>("Thành công", roles));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoleById(@PathVariable("id") int roleId) {
        try {
            Role existingRole = roleService.getRoleById(roleId);
            return ResponseEntity.ok(existingRole);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
