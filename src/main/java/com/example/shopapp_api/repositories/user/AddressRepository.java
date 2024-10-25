package com.example.shopapp_api.repositories.user;

import com.example.shopapp_api.entities.orders.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    //repository lưu trữ
    // Phương thức tìm dia chi theo userId
    List<Address> findByUserId(int userId);
}
