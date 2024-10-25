package com.example.shopapp_api.entities.users;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tự dong759 tăng
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    public static String ADMIN = "ADMIN";

    public static String USER = "USER";

}
