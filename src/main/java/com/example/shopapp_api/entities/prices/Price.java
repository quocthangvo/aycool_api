package com.example.shopapp_api.entities.prices;

import com.example.shopapp_api.entities.BaseEntity;
import com.example.shopapp_api.entities.products.ProductDetail;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "gia_ban")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Price extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tự dong759 tăng
    @Column(name = "ma_gia")
    private int id;

    @Column(name = "gia_ban", nullable = false)
    private Float sellingPrice;

    @Column(name = "gia_khuyen_mai")
    private Float promotionPrice;

    @Column(name = "ngay_bat_dau")
    private LocalDate startDate;

    @Column(name = "ngay_ket_thuc")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "ma_chi_tiet_san_pham")
    @JsonBackReference
    private ProductDetail productDetail;
}
