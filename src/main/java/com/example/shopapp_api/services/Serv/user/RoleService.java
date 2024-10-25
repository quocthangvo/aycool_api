package com.example.shopapp_api.services.Serv.user;

import com.example.shopapp_api.entities.users.Role;
import com.example.shopapp_api.repositories.user.RoleRepository;
import com.example.shopapp_api.services.Impl.user.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getRoleById(int id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quyền với id: " + id));
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
