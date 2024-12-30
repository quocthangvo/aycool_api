package com.example.shopapp_api.entities.products;

import com.example.shopapp_api.entities.attributes.Color;
import com.example.shopapp_api.entities.attributes.Size;
import com.example.shopapp_api.entities.cart.CartItem;
import com.example.shopapp_api.entities.prices.Price;
import com.example.shopapp_api.entities.review.Review;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "chi_tiet_san_pham")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tự dong759 tăng
    @Column(name = "ma_chi_tiet_san_pham")
    private int id;

    @Column(name = "ten_sku")
    private String skuName;

    @Column(name = "phien_ban_sku")
    private String skuVersion;

    @Column(name = "so_luong")
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "ma_san_pham")
    @JsonBackReference // bỏ qua id tránh lập lại
    private Product product;

    @ManyToOne
    @JoinColumn(name = "ma_mau_sac")
    private Color color;

    @ManyToOne
    @JoinColumn(name = "ma_kich_thuoc")
    private Size size;

    @OneToMany(mappedBy = "productDetail")
    @JsonManagedReference
    private List<Price> prices;


//    //cart
//    @OneToMany(mappedBy = "productDetail")
//    @JsonManagedReference
//    private CartItem cartItem;
}
