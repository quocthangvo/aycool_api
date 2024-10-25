package com.example.shopapp_api.entities.orders;

import com.example.shopapp_api.entities.products.Product;
import com.example.shopapp_api.entities.products.ProductDetail;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "order_details")
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
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "total_money", nullable = false)
    private Float totalMoney;

    @Column(name = "price", nullable = false)
    private Float price;
}
