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
@Table(name = "reviews")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tự dong tăng
    private int id;

    @JoinColumn(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @JoinColumn(name = "rating")
    private int rating;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;

    @JoinColumn(name = "status")
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
