package com.example.shopapp_api.entities.orders;

import com.example.shopapp_api.entities.users.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "dia_chi")
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tự động tăng
    @Column(name = "ma_dia_chi")
    private int id;

    @Column(name = "ho_ten", length = 100)
    private String fullName;

    @Column(name = "so_dien_thoai", nullable = false, length = 10)
    private String phoneNumber;

    @Column(name = "ten_duong")
    private String streetName;

    @Column(name = "thanh_pho", length = 100)
    private String city;

    @Column(name = "quan_huyen")
    private String district;

    @Column(name = "xa_phuong")
    private String ward;

    @ManyToOne
    @JoinColumn(name = "ma_nguoi_dung")
    @JsonBackReference
    private User user;
}
