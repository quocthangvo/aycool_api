package com.example.shopapp_api.entities.products;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "anh_san_pham")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class ProductImage {
    public static final int MAXIMUM_IMAGES_PER_PRODUCT = 6;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tự dong759 tăng
    @Column(name = "ma_anh")
    private int id;

    @Column(name = "duong_dan_anh", length = 250)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "ma_san_pham")
    @JsonBackReference // bỏ qua id tránh lập lại
    private Product product;
}
