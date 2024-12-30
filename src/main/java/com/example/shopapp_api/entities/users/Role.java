package com.example.shopapp_api.entities.users;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quyen")
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tự dong759 tăng
    @Column(name = "ma_quyen")
    private int id;

    @Column(name = "ten_quyen", nullable = false)
    private String name;

    public static String ADMIN = "ADMIN";

    public static String USER = "USER";

}
