package com.example.shopapp_api.repositories.user;

import com.example.shopapp_api.entities.users.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
