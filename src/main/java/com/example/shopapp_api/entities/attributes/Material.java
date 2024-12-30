package com.example.shopapp_api.entities.attributes;

import jakarta.persistence.*;
import lombok.*;

@Entity //đê biết là thực thể
@Table(name = "chat_lieu")
@Data
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor //khởi tạo mặc định k tham số
@Builder //khởi tạo từng thành phần

public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tự động tăng
    @Column(name = "ma_chat_lieu")
    private int id;

    @Column(name = "ten_chat_lieu", nullable = false, length = 250)
    private String name;
}
