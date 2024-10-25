package com.example.shopapp_api.entities.attributes;

import com.example.shopapp_api.entities.products.ProductDetail;
import jakarta.persistence.*;
import lombok.*;

@Entity //đê biết là thực thể
@Table(name = "colors")
@Data
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor //khởi tạo mặc định k tham số
@Builder //khởi tạo từng thành phần

public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tự động tăng
    private int id;

    @Column(name = "name", nullable = false, length = 250)
    private String name;


}
