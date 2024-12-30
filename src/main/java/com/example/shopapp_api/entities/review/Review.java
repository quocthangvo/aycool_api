package com.example.shopapp_api.entities.review;

import com.example.shopapp_api.entities.BaseEntity;
import com.example.shopapp_api.entities.attributes.Material;
import com.example.shopapp_api.entities.orders.Order;
import com.example.shopapp_api.entities.orders.OrderDetail;
import com.example.shopapp_api.entities.products.Product;
import com.example.shopapp_api.entities.products.ProductDetail;
import com.example.shopapp_api.entities.users.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "danh_gia")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tự dong tăng
    @Column(name = "ma_danh_gia")
    private int id;

    @Column(name = "binh_luan", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "so_sao")
    private int rating;

    @ManyToOne
    @JoinColumn(name = "ma_nguoi_dung")
    private User user;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "ma_san_pham")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "ma_don_hang")
    @JsonBackReference
    private Order order;

    @Column(name = "trang_thai")
    private boolean status;
//
//    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
//    private List<Reply> replies = new ArrayList<>();

//    @ManyToOne
//    @JoinColumn(name = "order_detail_id")
//    private OrderDetail orderDetail;

//    @ManyToOne
//    @JoinColumn(name = "product_id")
//    private Product product;


}
