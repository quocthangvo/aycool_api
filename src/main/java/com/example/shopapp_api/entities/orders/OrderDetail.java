package com.example.shopapp_api.entities.orders;

import com.example.shopapp_api.entities.products.Product;
import com.example.shopapp_api.entities.products.ProductDetail;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "chi_tiet_don_hang")
@Entity
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tự dong759 tăng
    @Column(name = "ma_chi_tiet_don_hang")
    private int id;

    @ManyToOne
    @JoinColumn(name = "ma_don_hang")
    @JsonBackReference
    private Order order;

    @ManyToOne
    @JoinColumn(name = "ma_chi_tiet_san_pham")
    private ProductDetail productDetail;

    @Column(name = "so_luong", nullable = false)
    private int quantity;

    @Column(name = "tong_tien", nullable = false)
    private Float totalMoney;

//    @Column(name = "price", nullable = false)
//    private Float price;
}
