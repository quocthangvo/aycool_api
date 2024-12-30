package com.example.shopapp_api.entities.attributes;

import com.example.shopapp_api.entities.products.ProductDetail;
import jakarta.persistence.*;
import lombok.*;

@Entity //đê biết là thực thể
@Table(name = "mau_sac")
@Data
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor //khởi tạo mặc định k tham số
@Builder //khởi tạo từng thành phần

public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tự động tăng
    @Column(name = "ma_mau_sac")
    private int id;

    @Column(name = "ten_mau_sac", nullable = false, length = 250)
    private String name;


}
