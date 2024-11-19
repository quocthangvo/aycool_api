package com.example.shopapp_api.entities.products;


import com.example.shopapp_api.entities.BaseEntity;
import com.example.shopapp_api.entities.attributes.Material;
import com.example.shopapp_api.entities.categories.SubCategory;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


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

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonManagedReference // đánh dấu ở productdetail là cha
    private List<ProductDetail> productDetails;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference //đánh dấu ở productImage là cha
    private List<ProductImage> productImages;
//    private List<MultipartFile> files;
    // fetch = FetchType.LAZY khi truy vấn product sẽ
    // không được tải cho đến khi bạn truy cập vào thuộc tính productImages
}
