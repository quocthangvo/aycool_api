package com.example.shopapp_api.entities.cart;

import com.example.shopapp_api.entities.products.Product;
import com.example.shopapp_api.entities.products.ProductDetail;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity //đê biết là thực thể
@Table(name = "chi_tiet_gio_hang")
@Data
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor //khởi tạo mặc định k tham số
@Builder
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_chi_tiet_gio_hang")
    private int id;

    @ManyToOne
    @JsonBackReference("cart-items")
    @JoinColumn(name = "ma_gio_hang")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "ma_chi_tiet_san_pham")
    private ProductDetail productDetail;

    @Column(name = "so_luong")
    private int quantity;
}
