package com.example.shopapp_api.entities.products;


import com.example.shopapp_api.entities.BaseEntity;
import com.example.shopapp_api.entities.attributes.Material;
import com.example.shopapp_api.entities.categories.SubCategory;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tự dong759 tăng
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "sku", nullable = false)
    private String sku;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;

//    private List<MultipartFile> files;
}
