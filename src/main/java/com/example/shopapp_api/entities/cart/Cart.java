package com.example.shopapp_api.entities.cart;

import com.example.shopapp_api.entities.users.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity //đê biết là thực thể
@Table(name = "gio_hang")
@Data
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor //khởi tạo mặc định k tham số
@Builder

public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tự động tăng
    @Column(name = "ma_gio_hang")
    private int id;

    @OneToOne
    @JoinColumn(name = "ma_nguoi_dung")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("cart-items")
    private List<CartItem> items = new ArrayList<>();

}
