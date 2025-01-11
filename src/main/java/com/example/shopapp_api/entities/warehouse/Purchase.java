package com.example.shopapp_api.entities.warehouse;

import com.example.shopapp_api.entities.BaseEntity;
import com.example.shopapp_api.entities.products.ProductDetail;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "nhap_hang")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tự dong tăng
    @Column(name = "ma_nhap_hang")
    private int id;

    @Column(name = "so_luong")
    private int quantity;

    @Column(name = "gia_nhap")
    private Float price;

    @ManyToOne

    @JoinColumn(name = "ma_chi_tiet_san_pham")
    private ProductDetail productDetail;

    @ManyToOne
//    @JsonBackReference
    @JoinColumn(name = "ma_kho") // Mối quan hệ nhiều với ProductDetail
    private Warehouse warehouse;

    @Column(name = "ngay_nhap")
    private LocalDateTime dateTime;
}
