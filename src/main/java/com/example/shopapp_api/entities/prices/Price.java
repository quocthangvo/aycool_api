package com.example.shopapp_api.entities.prices;

import com.example.shopapp_api.entities.BaseEntity;
import com.example.shopapp_api.entities.products.ProductDetail;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "prices")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Price extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tự dong759 tăng
    private int id;

    @Column(name = "selling_price", nullable = false)
    private Float sellingPrice;

    @Column(name = "promotion_price", nullable = false)
    private Float promotionPrice;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;
}
