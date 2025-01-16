package com.example.shopapp_api.entities.products;


import com.example.shopapp_api.entities.BaseEntity;
import com.example.shopapp_api.entities.attributes.Material;
import com.example.shopapp_api.entities.categories.SubCategory;
import com.example.shopapp_api.entities.review.Review;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Table(name = "san_pham")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tự dong759 tăng
    @Column(name = "ma_san_pham")
    private int id;

    @Column(name = "ten_san_pham", nullable = false)
    private String name;

    @Column(name = "sku", nullable = false)
    private String sku;


    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "ma_danh_muc_con")
    private SubCategory subCategory;

    @ManyToOne
    @JoinColumn(name = "ma_chat_lieu")
    private Material material;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonManagedReference // đánh dấu ở productdetail là cha
    private List<ProductDetail> productDetails;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference //đánh dấu ở productImage là cha
    private List<ProductImage> productImages;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Review> reviews;

//    private List<MultipartFile> files;
    // fetch = FetchType.LAZY khi truy vấn product sẽ
    // không được tải cho đến khi bạn truy cập vào thuộc tính productImages
}
