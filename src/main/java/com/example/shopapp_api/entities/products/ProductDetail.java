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
@Table(name = "product_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tự dong759 tăng
    private int id;

    @JoinColumn(name = "sku_name")
    private String skuName;

    @JoinColumn(name = "sku_version")
    private String skuVersion;

    @JoinColumn(name = "quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference // bỏ qua id tránh lập lại
    private Product product;

    @ManyToOne
    @JoinColumn(name = "color_id")
    private Color color;

    @ManyToOne
    @JoinColumn(name = "size_id")
    private Size size;

    @OneToMany(mappedBy = "productDetail")
    @JsonManagedReference
    private List<Price> prices;

    @OneToMany(mappedBy = "productDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Review> reviews;
//    //cart
//    @OneToMany(mappedBy = "productDetail")
//    @JsonManagedReference
//    private CartItem cartItem;
}
