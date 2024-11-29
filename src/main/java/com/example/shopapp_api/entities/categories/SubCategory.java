package com.example.shopapp_api.entities.categories;

import com.example.shopapp_api.entities.users.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder //khởi tạo từng thành phần
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sub_categories")

public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//id tự dong759 tăng
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "category_id")
    private Category category;
}
