package com.example.shopapp_api.repositories.user;

import com.example.shopapp_api.entities.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);

    //
    Optional<User> findByEmail(String email);
}
