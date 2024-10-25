package com.example.shopapp_api.services.Impl.user;

import com.example.shopapp_api.entities.users.Role;

import java.util.List;

public interface IRoleService {
    Role getRoleById(int id);

    List<Role> getAllRoles();
}
