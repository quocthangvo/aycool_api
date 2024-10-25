package com.example.shopapp_api.entities.products;

import com.example.shopapp_api.entities.attributes.Color;
import com.example.shopapp_api.entities.attributes.Size;
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

    @JoinColumn(name = "sku_version")
    private String skuVersion;

    @JoinColumn(name = "quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "color_id")
    private Color color;

    @ManyToOne
    @JoinColumn(name = "size_id")
    private Size size;


}
