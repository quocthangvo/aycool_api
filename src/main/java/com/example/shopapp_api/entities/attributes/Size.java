package com.example.shopapp_api.entities.attributes;

import com.example.shopapp_api.entities.products.ProductDetail;
import jakarta.persistence.*;
import lombok.*;

@Entity //đê biết là thực thể
@Table(name = "kich_thuoc")
@Data
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor //khởi tạo mặc định k tham số
@Builder //khởi tạo từng thành phần

public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tự động tăng
    @Column(name = "ma_kich_thuoc")
    private int id;

    @Column(name = "ten_kich_thuoc", nullable = false, length = 250)
    private String name;

    @Column(name = "mo_ta")
    private String description;

}
