package com.example.shopapp_api.entities.warehouse;


import com.example.shopapp_api.entities.BaseEntity;
import com.example.shopapp_api.entities.prices.Price;
import com.example.shopapp_api.entities.products.Product;
import com.example.shopapp_api.entities.products.ProductDetail;
import com.example.shopapp_api.entities.users.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "kho")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Warehouse extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tự dong tăng
    @Column(name = "ma_kho")
    private int id;

    @Column(name = "so_luong_tong")
    private int quantity;

    @Column(name = "gia_nhap")
    private Float price;

    @Column(name = "so_luong_ban")
    private int sellQuantity;

    @Column(name = "so_luong_con")
    private int remainingQuantity;


    @ManyToOne
//    @JsonBackReference
    @JoinColumn(name = "ma_chi_tiet_san_pham") // Mối quan hệ nhiều với ProductDetail
    private ProductDetail productDetail;

    @ManyToOne
    @JoinColumn(name = "ma_san_pham")
    private Product product;


}
