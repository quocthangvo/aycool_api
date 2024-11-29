package com.example.shopapp_api.entities.categories;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity //đê biết là thực thể
@Table(name = "categories")
@Data
@Getter
@Setter
@AllArgsConstructor //khởi tạo all
@NoArgsConstructor //khởi tạo mặc định k tham số
@Builder //khởi tạo từng thành phần

public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tự động tăng
    private int id;

    @Column(name = "name", nullable = false, length = 250)
    private String name;

    @OneToMany(mappedBy = "category")
    @JsonManagedReference
    private List<SubCategory> subCategories;
}
